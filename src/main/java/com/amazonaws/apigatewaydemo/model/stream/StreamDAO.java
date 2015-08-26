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

import com.amazonaws.apigatewaydemo.exception.DAOException;

import java.util.List;

/**
 * This interface defines the methods required for an implementation of the StreamDAO object
 */
public interface StreamDAO {
    /**
     * Creates a new stream in the data store
     *
     * @param stream The stream object to be created
     * @return The generated streamId
     * @throws DAOException Whenever an error occurs while accessing the data store
     */
    String createStream(Stream stream) throws DAOException;

    /**
     * Retrieves a Stream object by its id
     *
     * @param streamId The streamId to look for
     * @return An initialized and populated Stream object. If the stream couldn't be found return null
     * @throws DAOException Whenever a data store access error occurs
     */
    Stream getStreamById(String streamId) throws DAOException;

    List<Stream> getStreams(int limit);
}
