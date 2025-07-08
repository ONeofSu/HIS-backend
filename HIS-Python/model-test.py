import torch
from transformers import (
    AutoModelForCausalLM,
    AutoTokenizer,
    pipeline
)
from peft import PeftModel

# 基础模型路径（原始TinyLlama）
base_model = "TinyLlama/TinyLlama-1.1B-Chat-v1.0"
# 训练好的适配器路径（你的checkpoint目录）
adapter_path = "D:/Coding/Python/PythonProject/HIS/tinyllama-tcm-checkpoints/checkpoint-10000"

# 加载tokenizer（必须与训练时一致）
tokenizer = AutoTokenizer.from_pretrained(base_model, use_fast=True)
tokenizer.pad_token = tokenizer.eos_token  # 确保pad_token已设置

# 加载基础模型（4bit量化）
model = AutoModelForCausalLM.from_pretrained(
    base_model,
    device_map="auto",
    quantization_config={
        "load_in_4bit": True,
        "bnb_4bit_quant_type": "nf4",
        "bnb_4bit_compute_dtype": torch.float16
    }
)

# 加载LoRA适配器（关键！）
model = PeftModel.from_pretrained(model, adapter_path)
model = model.merge_and_unload()  # 合并适配器到基础模型（可选，提升推理速度）

# 创建文本生成管道
pipe = pipeline(
    "text-generation",
    model=model,
    tokenizer=tokenizer,
    torch_dtype=torch.float16,  # 半精度加速
    eos_token_id = None  # 禁用提前终止（防止遇到句号停止）
)

def format_prompt(query):
    return f"<|im_start|>user\n{query}<|im_end|>\n<|im_start|>assistant\n"

# 示例测试
# prompt = format_prompt("我头疼应该吃什么中药")
# output = pipe(
#     prompt,
#     max_new_tokens=100,
#     do_sample=True,
#     temperature=0.7,
#     top_p=0.9
# )
# print(output[0]["generated_text"])

while True:
    query = input("用户输入（输入q退出）: ")
    query = query.encode('utf-8').decode('utf-8')  # 确保UTF-8
    if query.lower() == "q":
        break
    prompt = format_prompt(query)
    output = pipe(prompt, max_new_tokens=500)[0]["generated_text"]
    assistant_reply = output.split("<|im_start|>assistant\n")[-1].replace("<|im_end|>", "")
    print(f"AI回答: {assistant_reply}\n")