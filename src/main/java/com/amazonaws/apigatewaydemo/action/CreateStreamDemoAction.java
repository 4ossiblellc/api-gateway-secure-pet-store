/*
 * Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.amazonaws.apigatewaydemo.action;

import com.amazonaws.apigatewaydemo.configuration.ExceptionMessages;
import com.amazonaws.apigatewaydemo.exception.BadRequestException;
import com.amazonaws.apigatewaydemo.exception.DAOException;
import com.amazonaws.apigatewaydemo.exception.InternalErrorException;
import com.amazonaws.apigatewaydemo.model.DAOFactory;
import com.amazonaws.apigatewaydemo.model.action.CreateStreamRequest;
import com.amazonaws.apigatewaydemo.model.action.CreateStreamResponse;
import com.amazonaws.apigatewaydemo.model.stream.Stream;
import com.amazonaws.apigatewaydemo.model.stream.StreamDAO;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonObject;

/**
 * Action that creates a new Stream in the data store
 * <p/>
 * POST to /streams/
 */
public class CreateStreamDemoAction extends AbstractDemoAction {
    private LambdaLogger logger;

    public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        logger = lambdaContext.getLogger();

        CreateStreamRequest input = getGson().fromJson(request, CreateStreamRequest.class);

        if (input == null ||
                input.getStreamType() == null ||
                input.getStreamType().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }

        StreamDAO dao = DAOFactory.getStreamDAO();

        Stream newStream = new Stream();
        newStream.setStreamType(input.getStreamType());
        newStream.setStreamName(input.getStreamName());
        newStream.setStreamAge(input.getStreamAge());

        String streamId;

        try {
            streamId = dao.createStream(newStream);
        } catch (final DAOException e) {
            logger.log("Error while creating new stream\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        if (streamId == null || streamId.trim().equals("")) {
            logger.log("StreamID is null or empty");
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        CreateStreamResponse output = new CreateStreamResponse();
        output.setStreamId(streamId);

        return getGson().toJson(output);
    }
}
