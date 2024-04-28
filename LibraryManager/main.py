from twilio.rest import Client
from flask import Flask, request, Response
from twilio.twiml.messaging_response import MessagingResponse
import Controller

account_sid = 'ACcbebfa04412c9fccc218767b1f1ddecf'
auth_token = '002efaf6e7205cf385022a470af5e6b6'
client = Client(account_sid, auth_token)

app = Flask(__name__)

@app.route('/webhook', methods=['POST'])
def reply():
    msg = request.form.get('Body')
    sender = request.form.get('From')
    array = sender.split(':')
        
    response_message = Controller.Handler(msg, array[1])
    
    message = client.messages.create(
    from_='whatsapp:+14155238886',
    body=response_message,
    to=sender
    )
    
    return 'Message Sent', 200

if __name__ == '__main__':
    app.run(port='3080')
