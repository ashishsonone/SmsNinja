# SmsNinja
Securely share you OTP SMS for shared accounts/subscriptions

## Flow
- It can selectively pick SMS (which match certain regex pattern - sender + body), then encrypt it using local key (`secretKey`) and publish it at URL (identified by `locationKey` e.g R4xZPuqC5). Together this configuration is called as `ForwardingRule`
- you share the `comboKey` = (`locationKey:secretKey`) with other party who you want to be able to access these messages
- They use the app, enter the combo key, and are able to decode the SMS published. We only keep the latest published SMS at a particular `locationKey` (overwritten)
- Anybody can access the URL (if they know the `locationKey`), but they'll look at gibberish encrypted content. They can only decrypt if they have the `secretKey` with them. Example content
```
DcuDIGOvfxpNt+GIaCUy2Q==
:KgNy0zy3G9n+S3GgVI6xKrfavO9lRZSMARBIEZrVu3RLe8x1/0M0ix3LzEyKXrbMrb5XNFOeP9B8
SzzGvUsEhSmTm/GXOwHsr91CEeX97fEuqCxUzSplFlBWXoY8/sD/nUuGtyIVGxqStMQayTBTVg==
```


## Extra Notes:
- Note: `secretKey` is a randomly generated 128 bit AES key (encoded using base64) e.g JbFVI1QnhPEGEfdg5oGm4A==
- Note: `locationKey` is a short id (7-14 charcters) generated by the server.
- Note: Only the owner device can publish content to `locationKey`, as it is tied to the owner's random `clientId` (UUID generated locally during first launch of app)
- Note: `secreteKey` is never sent to the server. It is stored locally on device. Only encrypted content is sent and stored at the `locationKey` url. And you're supposed to share the secret to your friend/family securely.


<p float="left">
  <img src="https://user-images.githubusercontent.com/1778327/168960610-a015f4c5-f6f9-41cd-94aa-e256a9e03de6.png" height="600" />
  <img src="https://user-images.githubusercontent.com/1778327/168960623-7ee4b545-227b-4a10-80c8-6e29c80da927.png" height="600" /> 
</p>
