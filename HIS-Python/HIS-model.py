import torch
from transformers import (
    AutoModelForCausalLM,
    AutoTokenizer,
    TrainingArguments,
    Trainer,
    pipeline,
    BitsAndBytesConfig,
    DataCollatorForLanguageModeling
)
from datasets import load_dataset
from peft import LoraConfig, get_peft_model

# 1. 加载数据集并预处理
print("load dataset")
dataset = load_dataset("michaelwzhu/ShenNong_TCM_Dataset")

def format_data(example):
    """确保输出是单字符串格式"""
    text = f"<|im_start|>user\n{example['query']}<|im_end|>\n<|im_start|>assistant\n{example['response']}<|im_end|>"
    return {"text": text}

dataset = dataset.map(format_data, remove_columns=["query", "response"])  # 移除原始列

# 2. 加载模型和tokenizer（关键修改：添加padding_side）
print("load model")
model_name = "TinyLlama/TinyLlama-1.1B-Chat-v1.0"
tokenizer = AutoTokenizer.from_pretrained(model_name, use_fast=True)
tokenizer.pad_token = tokenizer.eos_token
tokenizer.padding_side = "right"  # 重要！确保填充在右侧

# 3. Tokenize数据集（关键修改：更安全的处理方式）
def tokenize_function(examples):
    return tokenizer(
        examples["text"],
        truncation=True,
        max_length=512,
        padding="max_length",  # 固定长度填充
        return_tensors="pt"    # 直接返回PyTorch张量
    )

tokenized_dataset = dataset.map(
    tokenize_function,
    batched=True,
    remove_columns=["text"]    # 移除原始文本列
)

# 4. 添加LoRA适配器
print("add LORA")
model = AutoModelForCausalLM.from_pretrained(
    model_name,
    quantization_config=BitsAndBytesConfig(
        load_in_4bit=True,
        bnb_4bit_quant_type="nf4",
        bnb_4bit_compute_dtype=torch.float16
    ),
    device_map="auto"
)

peft_config = LoraConfig(
    r=8,
    lora_alpha=32,
    target_modules=["q_proj", "k_proj", "v_proj"],
    lora_dropout=0.05,
    bias="none",
    task_type="CAUSAL_LM"
)
model = get_peft_model(model, peft_config)
model.print_trainable_parameters()

# 5. 配置训练参数（关键修改：添加label_names）
print("save training args")
training_args = TrainingArguments(
    output_dir="./tinyllama-tcm-checkpoints",
    per_device_train_batch_size=4,
    gradient_accumulation_steps=4,
    num_train_epochs=3,
    learning_rate=3e-4,
    logging_steps=20,
    save_strategy="steps",
    save_steps=200,
    save_total_limit=3,
    fp16=True,
    report_to="none",
    label_names=["input_ids"]  # 重要！指定标签字段
)

# 6. 使用正确的DataCollator
data_collator = DataCollatorForLanguageModeling(
    tokenizer=tokenizer,
    mlm=False
)

# 7. 开始训练
print("start train")
trainer = Trainer(
    model=model,
    args=training_args,
    train_dataset=tokenized_dataset["train"],
    data_collator=data_collator
)

trainer.train()

# 8. 保存和测试
model.save_pretrained("./tinyllama-tcm-final")
tokenizer.save_pretrained("./tinyllama-tcm-final")

pipe = pipeline(
    "text-generation",
    model=model,
    tokenizer=tokenizer,
    device_map="auto"
)
print(pipe("黄芪的主要功效是", max_length=100)[0]["generated_text"])
