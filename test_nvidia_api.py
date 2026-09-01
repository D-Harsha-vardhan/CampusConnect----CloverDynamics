import urllib.request
import json
import ssl
import os

API_KEY = os.environ.get("NVIDIA_API_KEY", "")

if not API_KEY:
    API_KEY = input("Enter your NVIDIA API Key: ").strip()

url = "https://integrate.api.nvidia.com/v1/chat/completions"

text = """A total of **17 people** are working on or have research interests in **5G and 6G communication**, including both faculty and students. Notable faculty members with relevant expertise are **Dr. Anjali Verma**, **Dr. Yash Malhotra**, and **Dr. Dev Kapoor**, while students such as **Naveen Reddy**, **Rakesh Menon**, and **Tanya Kulkarni** are also involved in this area. Most of these individuals are from departments like Computer Science, Artificial Intelligence & Data Science, Information Technology, and Embedded Systems."""

system_prompt = "You are a data extraction tool. Extract human names from the text and return them as a comma-separated list. You must wrap your final comma-separated list in <names> and </names> tags."

data = {
    "model": "nvidia/nemotron-3.5-lightning-30b-a3b",
    "messages": [
        {"role": "system", "content": system_prompt},
        {"role": "user", "content": text}
    ],
    "max_tokens": 1024,
    "temperature": 0.0
}

req = urllib.request.Request(url, data=json.dumps(data).encode("utf-8"))
req.add_header("Authorization", f"Bearer {API_KEY}")
req.add_header("Content-Type", "application/json")

print("\nTesting NVIDIA API (Model: nvidia/nemotron-3.5-lightning-30b-a3b)...")
try:
    context = ssl._create_unverified_context()
    with urllib.request.urlopen(req, context=context) as response:
        result = json.loads(response.read().decode("utf-8"))
        choices = result.get("choices", [])
        if choices:
            message = choices[0].get("message", {}).get("content", "")
            print("\n[SUCCESS] API Success! Extracted Names:")
            print(message)
        else:
            print("\n[ERROR] API returned no choices.")
            print(result)
except Exception as e:
    print(f"\n[ERROR] API Test Failed: {e}")
