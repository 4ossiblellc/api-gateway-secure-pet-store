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
package com.amazonaws.apigatewaydemo.model.stream;

import com.amazonaws.apigatewaydemo.configuration.DynamoDBConfiguration;
import com.amazonaws.apigatewaydemo.exception.DAOException;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

import java.util.List;

/**
 * The DynamoDB implementation of the StreamDAO object. This class expects the Stream bean to be annotated with the required
 * DynamoDB Object Mapper annotations. Configuration values for the class such as table name and pagination rows is read
 * from the DynamoDBConfiguration class in the com.amazonaws.apigateway.configuration package. Credentials for the
 * DynamoDB client are read from the environment variables in the Lambda instance.
 * <p/>
 * This class is a singleton and should only be accessed through the static getInstance method. The constructor is defined
 * as protected.
 */
public class DDBStreamDAO implements StreamDAO {
    private static DDBStreamDAO instance = null;

    // credentials for the client come from the environment variables pre-configured by Lambda. These are tied to the
    // Lambda function execution role.
    private static AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient();

    /**
     * Returns the initialized default instance of the StreamDAO
     *
     * @return An initialized StreamDAO instance
     */
    public static DDBStreamDAO getInstance() {
        if (instance == null) {
            instance = new DDBStreamDAO();
        }

        return instance;
    }

    protected DDBStreamDAO() {
        // constructor is protected so that it can't be called from the outside
    }

    /**
     * Creates a new Stream
     *
     * @param stream The stream object to be created
     * @return The id for the newly created Stream object
     * @throws DAOException
     */
    public String createStream(Stream stream) throws DAOException {
        if (stream.getStreamType() == null || stream.getStreamType().trim().equals("")) {
            throw new DAOException("Cannot lookup null or empty stream");
        }

        getMapper().save(stream);

        return stream.getStreamId();
    }

    /**
     * Gets a Stream by its id
     *
     * @param streamId The streamId to look for
     * @return An initialized Stream object, null if the Stream could not be found
     * @throws DAOException
     */
    public Stream getStreamById(String streamId) throws DAOException {
        if (streamId == null || streamId.trim().equals("")) {
            throw new DAOException("Cannot lookup null or empty streamId");
        }

        return getMapper().load(Stream.class, streamId);
    }

    /**
     * Returns a list of streams in the DynamoDB table.
     *
     * @param limit The maximum numbers of results for the scan
     * @return A List of Stream objects
     */
    public List<Stream> getStreams(int limit) {
        if (limit <= 0 || limit > DynamoDBConfiguration.SCAN_LIMIT)
            limit = DynamoDBConfiguration.SCAN_LIMIT;

        DynamoDBScanExpression expression = new DynamoDBScanExpression();
        expression.setLimit(limit);
        return getMapper().scan(Stream.class, expression);
    }

    /**
     * Returns a DynamoDBMapper object initialized with the default DynamoDB client
     *
     * @return An initialized DynamoDBMapper
     */
    protected DynamoDBMapper getMapper() {
        return new DynamoDBMapper(ddbClient);
    }
}
