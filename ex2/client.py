import socket
import threading
import tkinter as tk
from tkinter import scrolledtext, messagebox

# Client functionality
def connect_to_server():
    global client_socket
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try:
        client_socket.connect(('localhost', 12345))
        messagebox.showinfo("Connection", "Connected to the server")
    except:
        messagebox.showerror("Connection Error", "Could not connect to the server.")
        root.quit()

    def receive():
        while True:
            try:
                message = client_socket.recv(1024).decode()
                if message:
                    chat_box.config(state=tk.NORMAL)
                    chat_box.insert(tk.END, f"Server: {message}\n")
                    chat_box.config(state=tk.DISABLED)
                    chat_box.yview(tk.END)
            except:
                break

    threading.Thread(target=receive, daemon=True).start()

def send_message():
    message = message_input.get()
    if message:
        client_socket.send(message.encode())
        chat_box.config(state=tk.NORMAL)
        chat_box.insert(tk.END, f"You: {message}\n")
        chat_box.config(state=tk.DISABLED)
        chat_box.yview(tk.END)
        message_input.set("")

def on_closing():
    global client_socket
    if client_socket:
        client_socket.close()  # Close the connection to the server
    root.quit()  # Close the GUI

# Tkinter GUI setup
root = tk.Tk()
root.title("Client Chat")

chat_box = scrolledtext.ScrolledText(root, width=50, height=20, state=tk.DISABLED)
chat_box.pack(pady=10)

message_input = tk.StringVar()
message_entry = tk.Entry(root, textvariable=message_input, width=40)
message_entry.pack(pady=5)

send_button = tk.Button(root, text="Send", command=send_message)
send_button.pack()

# Bind the window close event to the on_closing function
root.protocol("WM_DELETE_WINDOW", on_closing)

# Connect to server thread
threading.Thread(target=connect_to_server, daemon=True).start()

root.mainloop()
