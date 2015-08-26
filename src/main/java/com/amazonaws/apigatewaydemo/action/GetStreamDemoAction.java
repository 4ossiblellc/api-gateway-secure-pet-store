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
import com.amazonaws.apigatewaydemo.model.action.GetStreamRequest;
import com.amazonaws.apigatewaydemo.model.stream.Stream;
import com.amazonaws.apigatewaydemo.model.stream.StreamDAO;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonObject;

/**
 * Action that extracts a stream from the data store based on the given streamId
 * <p/>
 * GET to /streams/{streamId}
 */
public class GetStreamDemoAction extends AbstractDemoAction {
    private static LambdaLogger logger;

    public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        logger = lambdaContext.getLogger();

        GetStreamRequest input = getGson().fromJson(request, GetStreamRequest.class);

        if (input == null ||
                input.getStreamId() == null ||
                input.getStreamId().trim().equals("")) {
            logger.log("Invalid input passed to " + this.getClass().getName());
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }

        StreamDAO dao = DAOFactory.getStreamDAO();
        Stream stream;
        try {
            stream = dao.getStreamById(input.getStreamId());
        } catch (final DAOException e) {
            logger.log("Error while fetching stream with id " + input.getStreamId() + "\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        return getGson().toJson(stream);
    }
}
