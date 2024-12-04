
from flask import Flask, request, jsonify
from openai import OpenAI
import json
import dashscope

app = Flask(__name__)

dashscope.api_key = "sk-2ca9a4b9274c4c48b89afa6b6733153b"

def get_response(back_ground, question):
    client = OpenAI(
        api_key=dashscope.api_key,  # 如果您没有配置环境变量，请在此处用您的API Key进行替换
        base_url="https://dashscope.aliyuncs.com/compatible-mode/v1",  # 填写DashScope服务的base_url
    )
    completion = client.chat.completions.create(
        model="qwen-turbo",
        messages=[
            {'role': 'system', 'content': '你是一个参加公务员考试的考生，请根据问题给出你的答案'},
            {'role': 'user', 'content': back_ground + question}
        ],
        temperature=0.8,
        top_p=0.8
    )
    return json.loads(completion.model_dump_json())['choices'][0]['message']['content']

@app.route('/get_response', methods=['GET'])
def handle_request():
    back_ground = request.args.get('back_ground')
    question = request.args.get('question')
    if not back_ground or not question:
        return jsonify({'error': '缺少必要的参数'}), 400
    response = get_response(back_ground, question)
    return jsonify({'response': response})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)  # 监听所有网络接口，端口为5000