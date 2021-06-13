FROM python

RUN apt-get update 
RUN apt-get install -y python3-pip
RUN pip install flask
RUN pip install py_eureka_client
RUN pip install flask_cors

COPY ./*.py /home/app/paymentService.py

WORKDIR /home/app

CMD ["python3", "paymentService.py"]