import flask
from flask import request, Response
from flask import jsonify
import py_eureka_client.eureka_client as eureka_client
import os
import time
from flask_cors import cross_origin

class PaymentController:

	app = flask.Flask(__name__);
	app.config["DEBUG"] = True;
	app.config['CORS_HEADERS'] = 'Content-Type'

	def run(self):
		self.app.run(host='0.0.0.0');
		

	@app.route('/pay', methods=['POST'])
	def getDataWithFields():
		jsonValue = request.get_json();
		if (jsonValue['cvv'] < 500):
			return jsonify(False);
		return jsonify(True);

shouldTry = True;

while(shouldTry):
	try:
		time.sleep(120);
		eureka_client.init(eureka_server=os.getenv("EUREKA_CLIENT_SERVICEURL_DEFAULTZONE"),
						app_name="payment-service",
						instance_port=5000);
		shouldTry = False;
	except:
		print("Registering failed. Trying again...")
				   
PaymentController().run();