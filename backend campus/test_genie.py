import os
import asyncio
import httpx
from dotenv import load_dotenv

load_dotenv()

host = os.getenv("DATABRICKS_HOST", "").rstrip("/")
token = os.getenv("DATABRICKS_TOKEN", "")
space = os.getenv("GENIE_SPACE_ID", "")

headers = {
    "Authorization": f"Bearer {token}",
    "Content-Type": "application/json",
}

async def main():
    print(f"Connecting to Genie Space: {space} on {host}...")
    async with httpx.AsyncClient(timeout=60.0) as client:
        # 1. Start Conversation
        start_url = f"{host}/api/2.0/genie/spaces/{space}/start-conversation"
        payload = {"content": "Who has worked on edge AI projects?"}
        r = await client.post(start_url, json=payload, headers=headers)
        print("Start Status:", r.status_code)
        data = r.json()
        print("Start Response:", data)
        
        conv_id = data.get("conversation_id")
        msg_id = data.get("id") or data.get("message_id")
        
        if not conv_id or not msg_id:
            print("Could not get conversation or message ID")
            return
            
        print(f"Polling message status for conv={conv_id}, msg={msg_id}...")
        for i in range(10):
            await asyncio.sleep(3)
            poll_url = f"{host}/api/2.0/genie/spaces/{space}/conversations/{conv_id}/messages/{msg_id}"
            poll_res = await client.get(poll_url, headers=headers)
            print(f"Poll #{i+1} [{poll_res.status_code}]:", poll_res.json().get("status"))
            status = poll_res.json().get("status")
            if status in ["COMPLETED", "EXECUTED", "SUCCESS"]:
                import json
                print("Full Genie Poll Response JSON:")
                print(json.dumps(poll_res.json(), indent=2))
                break
            elif status in ["FAILED", "ERROR"]:
                print("Genie Error:", poll_res.json())
                break

if __name__ == "__main__":
    asyncio.run(main())
