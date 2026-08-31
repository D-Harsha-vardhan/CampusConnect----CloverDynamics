-- Create the messages table
CREATE TABLE messages (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    connection_id TEXT NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    sender_id TEXT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

-- Enable Row Level Security
ALTER TABLE messages ENABLE ROW LEVEL SECURITY;

-- Create policy to allow all users (Anon) to select messages
CREATE POLICY "Allow anonymous read" ON messages
    FOR SELECT
    TO anon
    USING (true);

-- Create policy to allow all users (Anon) to insert messages
CREATE POLICY "Allow anonymous insert" ON messages
    FOR INSERT
    TO anon
    WITH CHECK (true);

-- Enable Realtime for the messages table
BEGIN;
  DROP PUBLICATION IF EXISTS supabase_realtime;
  CREATE PUBLICATION supabase_realtime;
COMMIT;
ALTER PUBLICATION supabase_realtime ADD TABLE messages;
