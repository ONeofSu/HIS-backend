from flask import Flask, request, jsonify
import torch
import re
from transformers import pipeline, AutoTokenizer, AutoModelForCausalLM
from peft import PeftModel
import logging

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


def clean_input(text):
    text = text.encode('utf-8').decode('utf-8')
    return re.sub(r'[^\w\s\u4e00-\u9fff]', '', text).strip()


def extract_reply(output):
    reply = output.split("<|im_start|>assistant\n")[-1]
    return reply.replace("<|im_end|>", "").strip()


class ModelWrapper:
    def __init__(self):
        self.pipe = None

    def load(self):
        if self.pipe is None:
            model_path = "TinyLlama/TinyLlama-1.1B-Chat-v1.0"
            adapter_path = "tinyllama-tcm-checkpoints/checkpoint-10000"

            tokenizer = AutoTokenizer.from_pretrained(model_path, use_fast=True)
            tokenizer.pad_token = tokenizer.eos_token

            model = AutoModelForCausalLM.from_pretrained(
                model_path,
                device_map="auto",
                quantization_config={
                    "load_in_4bit": True,
                    "bnb_4bit_quant_type": "nf4",
                    "bnb_4bit_compute_dtype": torch.float16
                }
            )
            model = PeftModel.from_pretrained(model, adapter_path)
            model = model.merge_and_unload()

            self.pipe = pipeline(
                "text-generation",
                model=model,
                tokenizer=tokenizer,
                device_map="auto",
                torch_dtype=torch.float16
            )


wrapper = ModelWrapper()


@app.route('/generate', methods=['POST'])
def generate():
    try:
        data = request.get_json()
        query = clean_input(data['query'])
        max_tokens = data.get('max_tokens', 500)

        prompt = f"<|im_start|>user\n{query}<|im_end|>\n<|im_start|>assistant\n"
        output = wrapper.pipe(
            prompt,
            max_new_tokens=max_tokens,
            do_sample=True,
            temperature=0.7,
            top_p=0.9,
            repetition_penalty=1.1
        )[0]["generated_text"]

        return jsonify({
            "status": "success",
            "response": extract_reply(output)
        })
    except Exception as e:
        logger.error(f"Error: {str(e)}")
        return jsonify({"status": "error", "message": str(e)}), 500


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8000)
