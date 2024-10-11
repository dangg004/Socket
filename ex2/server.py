import socket
import threading
import tkinter as tk
from tkinter import scrolledtext, messagebox

# Global variable for the connection socket
conn = None

# Server functionality
def start_server():
    global conn
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind(('localhost', 12345))
    server_socket.listen(1)
    conn, addr = server_socket.accept()
    messagebox.showinfo("Connection", f"Connected to {addr}")

    def receive():
        while True:
            try:
                message = conn.recv(1024).decode()
                if message:
                    chat_box.config(state=tk.NORMAL)
                    chat_box.insert(tk.END, f"Client: {message}\n")
                    chat_box.config(state=tk.DISABLED)
                    chat_box.yview(tk.END)
            except:
                break

    threading.Thread(target=receive, daemon=True).start()

def send_message():
    global conn
    message = message_input.get()
    if message and conn:
        conn.send(message.encode())
        chat_box.config(state=tk.NORMAL)
        chat_box.insert(tk.END, f"You: {message}\n")
        chat_box.config(state=tk.DISABLED)
        chat_box.yview(tk.END)
        message_input.set("")

def on_closing():
    global conn
    if conn:
        conn.close()  # Close the connection to the client
    root.quit()  # Close the GUI

# Tkinter GUI setup
root = tk.Tk()
root.title("Server Chat")

chat_box = scrolledtext.ScrolledText(root, width=50, height=20, state=tk.DISABLED)
chat_box.pack(pady=10)

message_input = tk.StringVar()
message_entry = tk.Entry(root, textvariable=message_input, width=40)
message_entry.pack(pady=5)

send_button = tk.Button(root, text="Send", command=send_message)
send_button.pack()

# Bind the window close event to the on_closing function
root.protocol("WM_DELETE_WINDOW", on_closing)

# Start server thread
threading.Thread(target=start_server, daemon=True).start()

root.mainloop()
