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

import com.amazonaws.apigatewaydemo.configuration.DynamoDBConfiguration;
import com.amazonaws.apigatewaydemo.exception.BadRequestException;
import com.amazonaws.apigatewaydemo.exception.InternalErrorException;
import com.amazonaws.apigatewaydemo.model.DAOFactory;
import com.amazonaws.apigatewaydemo.model.action.ListStreamsResponse;
import com.amazonaws.apigatewaydemo.model.stream.Stream;
import com.amazonaws.apigatewaydemo.model.stream.StreamDAO;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Action to return a list of streams from in the data store
 * <p/>
 * GET to /streams/
 */
public class ListStreamsDemoAction extends AbstractDemoAction {
    private static LambdaLogger logger;

    public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        logger = lambdaContext.getLogger();

        StreamDAO dao = DAOFactory.getStreamDAO();

        List<Stream> streams = dao.getStreams(DynamoDBConfiguration.SCAN_LIMIT);

        ListStreamsResponse output = new ListStreamsResponse();
        output.setCount(streams.size());
        output.setPageLimit(DynamoDBConfiguration.SCAN_LIMIT);
        output.setStreams(streams);

        return getGson().toJson(output);
    }
}
