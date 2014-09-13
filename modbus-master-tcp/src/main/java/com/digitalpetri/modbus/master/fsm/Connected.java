/*
 * Copyright 2014 Kevin Herron
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.digitalpetri.modbus.master.fsm;

import java.util.concurrent.CompletableFuture;

import io.netty.channel.Channel;

public class Connected implements ConnectionState {

    private final CompletableFuture<Channel> channelFuture;

    public Connected(CompletableFuture<Channel> channelFuture) {
        this.channelFuture = channelFuture;
    }

    @Override
    public ConnectionState transition(ConnectionEvent event, StateContext context) {
        switch (event) {
            case ChannelClosed:
                return new Disconnected();

            case DisconnectRequested:
                channelFuture.thenAccept(ch -> ch.close());
                return new Disconnected();

            default:
                return context.getState();
        }
    }

    @Override
    public CompletableFuture<Channel> channelFuture() {
        return channelFuture;
    }

}
