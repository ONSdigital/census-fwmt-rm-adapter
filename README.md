[![Build Status](https://travis-ci.org/ONSdigital/fwmt-rm-adapter.svg?branch=master)](https://travis-ci.org/ONSdigital/fwmt-rm-adapter) [![codecov](https://codecov.io/gh/ONSdigital/fwmt-rm-adapter/branch/master/graph/badge.svg)](https://codecov.io/gh/ONSdigital/fwmt-rm-adapter) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/9a61e9e47fef456894559330ba96b82c)](https://www.codacy.com/app/ONSDigital_FWMT/census-fwmt-rm-adapter?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ONSdigital/census-fwmt-rm-adapter&amp;utm_campaign=Badge_Grade)
# census-fwmt-rm-adapter

This service is a gateway between the Response Management System and the FWMT job service.

It takes an Action Instruction (Create, Update or Delete Request) message off the Action.Field RabbitMQ Queue and transforms it into a Field Worker Job Request Canonical message and places it onto the Gateway.Actions RabbitMQ Queue.


![](/rmadapter-highlevel.png "rmadapter highlevel diagram")	

## Quick Start

Requires RabbitMQ to start:

	docker run --name rabbit -p 5671-5672:5671:5672 -p 15671-15672:15671-15672 -d rabbitmq:3.6-management

To run:

	./gradlew bootRun

## rm-canonical mapping

![](/canonical-rm-mapping.png "canonical - rm - mapping")	


## Copyright
Copyright (C) 2018 Crown Copyright (Office for National Statistics)

