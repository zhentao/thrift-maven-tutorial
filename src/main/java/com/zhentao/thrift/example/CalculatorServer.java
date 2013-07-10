package com.zhentao.thrift.example;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;

import tutorial.Calculator;

public class CalculatorServer {

    public static void main(String[] args) {
        try {
            Calculator.Processor<CalculatorHandler> processor = new Calculator.Processor<CalculatorHandler>(
                                            new CalculatorHandler());
            startServer(processor);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    private static void startServer(Calculator.Processor<CalculatorHandler> processor) throws TTransportException {
        // TServerTransport serverTransport = new TServerSocket(9090);
        // TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

        // Use this for a multithreaded server
        // TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

        TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(9090);
        //TServer server = new THsHaServer(new THsHaServer.Args(serverTransport).workerThreads(20).processor(processor));
        TServer server = new TThreadedSelectorServer(new TThreadedSelectorServer.Args(serverTransport).workerThreads(20).selectorThreads(4).acceptQueueSizePerThread(8).processor(processor));
        System.out.println("Starting the simple server...");
        server.serve();
        System.out.println("started");
    }
}
