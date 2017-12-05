import React, { Component } from 'react';
import {
    AppRegistry,
    Text,
    View,
    TextInput,
    TouchableOpacity,
    FlatList
} from 'react-native';

import styling from './styles';

export default class onetoone extends Component {

    //async componentwDidMount() {
    // let response = await fetch('http://10.0.2.2:8080/onetoone/app/onetoone/get');
    // let responseJson = await response.json();
    // console.log(responseJson.typedText);
    // this.state.chatMessage = responseJson.typedText;
    //      //console.log(newMessage);
    // }
    // //https://facebook.github.io/react-native/movies.json

    // try {
    //     let response = await fetch('http://10.0.2.2:8080/onetoone/app/onetoone/get');
    //     let responseJson = await response.json();
    //     console.log(responseJson);
    //   } catch(error) {
    //     console.error(error);
    //   }

    state = {
        typedText: '',
        responseTextforSend: '',
        chatMessageArray: []
    }

    componentDidMount() {
        this.retrieveMessageTest();
    }

    updateChatBox(writtenText) {
        this.state.typedText = writtenText;
    }

    async messageSendButton() {
        // this.state.chatMessage = this.state.typedText;
        // this.state.chatMessageArray.push(this.state.chatMessage)
        // console.log(this.state.chatMessageArray);
        // this.setState(this.state);

        const chatMessage = this.state.typedText;
        const chatSending = {
            outgoing: chatMessage
        };
        this.state.chatMessageArray.push(chatSending);        
        let response = await fetch('http://10.0.2.2:8080/onetoone/app/otoo/send', {
            method: 'POST',
            headers: {
                'Accept': 'text/plain',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                id: 'car',
                from: 'bbb',
                to: 'aaa',
                typedText: this.state.typedText
            })
        });
        let responseText = await response.text();
        //console.log("The response is " + responseText);
        this.state.responseTextforSend = responseText;
        this.setState(this.state);
        this.clearText('textBox');
    }

    async retrieveMessageTest() {
        debugger;
        try {
            let response = await fetch('http://10.0.2.2:8080/onetoone/app/otoo/receive', {
                method: 'POST',
                headers: {
                    'Accept': 'text/plain',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    id: 'car',
                    from: 'bbb',
                    to: 'aaa',
                })
            });
            let responseText = await response.text();
            //console.log("the return message is " + responseText);
            var messageCount = 1;

            const chatM = {
                incoming: responseText
            };
            debugger;
            this.state.chatMessageArray.push(chatM);
            console.log (this.state.chatMessageArray);
            this.setState(this.state);
        } catch (error) {
            console.error(error);
        }
        this.retrieveMessageTest();
        this.setState(this.state);
    }

    clearText(fieldName) {
        this.refs[fieldName].clear(0);
    }

    renderMessagesOnTheScreen(item) {
        //debugger;
        //console.log(item);
        if (item.outgoing != null) {
            return (
                <View>
                    <View style={{ alignItems: 'flex-end', justifyContent: 'flex-start', backgroundColor: '#fff', paddingLeft: 30 }}>
                        <View style={{ marginLeft: 5, marginTop: 5, backgroundColor: '#d3d3d3', marginRight: 5, borderRadius: 7, borderColor: "black", borderWidth: 1, paddingLeft: 5, paddingRight: 5, paddingTop: 1, paddingBottom: 1 }} >
                            <Text>{item.outgoing}</Text>
                        </View>
                    </View>
                </View>);
        }
        if (item.incoming != null) {
            return (
                <View>
                    <View style={{ alignItems: 'flex-start', justifyContent: 'flex-start', backgroundColor: '#fff', paddingRight: 30 }}>
                        <View style={{ marginLeft: 5, marginTop: 5, backgroundColor: 'beige', marginRight: 5, borderRadius: 7, borderColor: "black", borderWidth: 1, paddingLeft: 5, paddingRight: 5, paddingTop: 1, paddingBottom: 1 }} >
                            <Text>{item.incoming}</Text>
                        </View>
                    </View>
                </View>);
        }
    }

    render() {
        return (
            <View style={{ flexDirection: 'column', flex: 1 }}>
                <View style={{ flex: 0.5 }}>
                    <Text> One2One </Text>
                </View>
                <View style={{ flex: 9.5 }}>
                    <View style={{ flexDirection: 'column', flex: 1 }}>
                        <View style={{ flex: 9.25, borderColor: 'black', borderWidth: 2 }}>
                            <FlatList
                                refreshing="true"
                                data={this.state.chatMessageArray}
                                extraData={this.state}
                                renderItem={({ item }) => this.renderMessagesOnTheScreen(item)}
                            />
                        </View>
                        <View style={{ flex: 0.75, borderColor: 'black', borderWidth: 2, flexDirection: 'row' }}>
                            <View style={{ flex: 9 }}>
                                <TextInput
                                    ref='textBox'
                                    placeholder='Type here'
                                    onChangeText={(text) => this.updateChatBox(text)}
                                />
                            </View >
                            <View style={{ flex: 1 }}>
                                <TouchableOpacity onPress={this.messageSendButton.bind(this)} >
                                    <Text style={{ fontSize: 30 }}> > </Text>
                                </TouchableOpacity>
                            </View>
                        </View>
                    </View>
                </View>
            </View>
        );
    }
}
AppRegistry.registerComponent('onetoone', () => onetoone);


//                                data={[{kkk: [ {greeting: "hai"}]}, {kkk: ["lol", "rofl"]}]}
