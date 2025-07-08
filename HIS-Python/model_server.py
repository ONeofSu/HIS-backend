from flask import Flask, request, jsonify
import torch
from transformers import AutoModelForCausalLM, AutoTokenizer, pipeline
from peft import PeftModel
import logging
from functools import wraps

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# 全局模型变量
model = None
tokenizer = None
pipe = None


def initialize_models():
    """初始化模型和pipeline"""
    global model, tokenizer, pipe

    logger.info("开始加载模型...")

    # 基础模型路径
    base_model = "TinyLlama/TinyLlama-1.1B-Chat-v1.0"
    # 适配器路径
    adapter_path = "D:/Coding/Python/PythonProject/HIS/tinyllama-tcm-checkpoints/checkpoint-10000"

    try:
        # 加载tokenizer
        tokenizer = AutoTokenizer.from_pretrained(base_model, use_fast=True)
        tokenizer.pad_token = tokenizer.eos_token

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

        # 加载LoRA适配器
        model = PeftModel.from_pretrained(model, adapter_path)
        model = model.merge_and_unload()

        # 创建文本生成管道（改进生成参数）
        pipe = pipeline(
            "text-generation",
            model=model,
            tokenizer=tokenizer,
            torch_dtype=torch.float16,
            eos_token_id=tokenizer.eos_token_id,  # 启用EOS标记检测
            device_map="auto"
        )

        logger.info("模型加载完成！")
        return True
    except Exception as e:
        logger.error(f"模型加载失败: {str(e)}")
        return False


def format_prompt(query):
    """格式化用户输入为模型接受的prompt"""
    return f"<|im_start|>user\n{query}<|im_end|>\n<|im_start|>assistant\n"

def extract_assistant_reply(output_text):
    """改进版：提取所有助手回复"""
    try:
        # 提取所有助手回复段
        segments = []
        parts = output_text.split("<|im_start|>assistant\n")
        for part in parts[1:]:  # 跳过第一部分(用户输入)
            reply = part.split("<|im_end|>")[0].strip()
            if reply:  # 只添加非空回复
                segments.append(reply)
        return "\n\n".join(segments) if segments else output_text
    except:
        return output_text



def require_models(f):
    """装饰器：确保模型已加载"""

    @wraps(f)
    def decorated_function(*args, **kwargs):
        if pipe is None:
            return jsonify({"error": "模型未初始化"}), 503
        return f(*args, **kwargs)

    return decorated_function


@app.route('/generate', methods=['POST'])
@require_models
def generate():
    """生成文本的API端点（改进版）"""
    try:
        data = request.get_json()
        query = data.get('query', '').strip()

        if not query:
            return jsonify({"error": "请输入查询内容"}), 400

        # 格式化prompt
        prompt = format_prompt(query)

        # 改进的生成参数（防止截断）
        output = pipe(
            prompt,
            max_new_tokens=512,  # 增加最大token数
            min_new_tokens=100,  # 设置最小token数
            do_sample=True,
            temperature=0.7,
            top_p=0.9,
            repetition_penalty=1.1,  # 防止重复
            num_return_sequences=1,
            pad_token_id=tokenizer.eos_token_id,
            early_stopping=True  # 遇到EOS标记停止
        )

        # 提取助手的回复
        full_output = output[0]["generated_text"]
        assistant_reply = extract_assistant_reply(full_output)

        # 日志记录完整输出（调试用）
        logger.info(f"Input: {query}")
        logger.info(f"Full Output: {full_output}")
        logger.info(f"Extracted Reply: {assistant_reply}")

        return jsonify({
            "response": assistant_reply,
            "status": "success"
        })

    except Exception as e:
        logger.error(f"生成错误: {str(e)}")
        return jsonify({"error": str(e)}), 500


@app.route('/health', methods=['GET'])
def health_check():
    """健康检查端点"""
    if pipe is not None:
        return jsonify({"status": "healthy", "model_loaded": True})
    return jsonify({"status": "unhealthy", "model_loaded": False}), 503


if __name__ == '__main__':
    # 启动时初始化模型
    if initialize_models():
        app.run(host='0.0.0.0', port=8000, threaded=True)
    else:
        logger.error("服务启动失败：模型初始化未完成")
