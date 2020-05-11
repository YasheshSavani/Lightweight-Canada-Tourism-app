# Author: Yashesh Savani
# Cloud Team 12
# Date Created: 29th-March-2020
from flask import Flask, jsonify
from flask import request
import pymysql
import pymysql.cursors as cursors
import pdfkit
import logging
import datetime
from flask_cors import CORS
import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.application import MIMEApplication
from email.encoders import encode_base64
from email.mime.text import MIMEText
app = Flask(__name__)
CORS(app)

@app.route('/ticketcreation', methods=['POST'])
def ticket_creation():
    print(request.json)
    email = str(request.json['email'])
    destination_name = str(request.json['destination_name']).capitalize()
    city = str(request.json['city']).capitalize()
    province = str(request.json['province']).upper()
    date_booked = str(datetime.datetime.now())
    date_travel = str(request.json['date_travel'])
    ticket_price = str(request.json['ticket_price'])
    passenger_number = str(request.json['passenger_number'])
    source = str(request.json['source']).capitalize()
    busid = str(request.json['busid'])
    total = int(passenger_number) * int(ticket_price)
    print(email, destination_name, city, province, date_booked,
          date_travel, ticket_price, passenger_number)

    while True:
        # Connect to database
        conn = pymysql.connect(host='tourismdb.cefqm5hgun7y.us-east-1.rds.amazonaws.com', port=3306,
                                    user='admin', password='toor12345', db='tourist', charset="utf8mb4",
                                    cursorclass=cursors.DictCursor)
        if conn:
            try:
                cursor = conn.cursor()
                # data access

                # Insert User data into table on registration
                insert_query = 'INSERT INTO Ticketdetail (Email,Destination_name,\
                    City,Province,Date_booked,Date_travel,Ticket_price,Passenger_number,Total, Source, Busid) \
                                VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)'

                cursor.execute(insert_query, (email, destination_name,
                                              city, province, date_booked, date_travel, str(ticket_price), str(passenger_number), str(total), source, busid))
                conn.commit()
                """
                html = "<!DOCTYPE html><html><body>  <h2>Email: {}</h2>  <h2>Source :  {} </h2>  <h2>Destination :  {} </h2>\
                          <h2>City :  {} </h2>  <h2>Province :  {} </h2>  <h2>Date Booked :  {} </h2> <h2>Date Travel :  {} </h2> \
                          <h2>Total Passengers :  {} </h2> <h2>Ticket has been confirmed. </h2> </body></html>".format(email,source,destination_name,city,province,date_booked,date_travel,passenger_number)

                with open("ticket.html", "w+") as f:
                    f.write(html)

                HTML('ticket.html').write_pdf('ticket.pdf')
                """
                msg = MIMEMultipart()
                msg['To'] = email
                msg['Subject'] = 'Ticket Confirmation'

                body = 'Your booking has been confirmed Email : {} , Source {} , Destination {} , City {} , Province {} , Date Travel {} , Total Passengers {}, Total Price: ${}'.format(email,source,destination_name,city,province,date_travel,passenger_number, total)
                """
                directory = 'ticket.pdf'
                with open(directory, 'rb') as opened:
                    opened_file = opened.read()

                msg.attach(MIMEText(body,'plain'))
                attached_file = MIMEApplication(opened_file, _subtype='pdf', _encoder=encode_base64)
                attached_file.add_header('content-disposition', 'attachment', filename="Ticket.pdf")
                msg.attach(attached_file)
                """
                msg.attach(MIMEText(body,'plain'))

                mailer = smtplib.SMTP("smtp.gmail.com:587")
                mailer.ehlo()
                mailer.starttls()
                mailer.login("travelapp.canada@gmail.com", 'group1220')
                mailer.sendmail("travelapp.canada@gmail.com", email, msg.as_string())
                mailer.quit()
            finally:
                conn.close()
            break
        else:
            logging.log(
                msg='Unable to connect to Ticket detail\n Trying to reconnect...', level=logging.DEBUG)
    return jsonify({'Result': 'Data Inserted'})


@app.route('/gettickets', methods=['GET'])
def get_tickets():

    email = str(request.args.get('email'))

    while True:
        # Connect to database
        conn = pymysql.connect(host='tourismdb.cefqm5hgun7y.us-east-1.rds.amazonaws.com', port=3306,
                                    user='admin', password='toor12345', db='tourist', charset="utf8mb4",
                                    cursorclass=cursors.DictCursor)
        if conn:

            try:
                cursor = conn.cursor()
                select_all_data = 'SELECT * from Ticketdetail WHERE Email = %s'

                cursor.execute(select_all_data, (str(email)))
                conn.commit()
                result = cursor.fetchall()
            finally:
                conn.close()
            break
        else:
            logging.log(
                msg='Unable to connect to Ticket detail\n Trying to reconnect...', level=logging.DEBUG)
    return jsonify({'Result': result})


if __name__ == "__main__":

    app.run(host="0.0.0.0", port=5001, debug=True)
