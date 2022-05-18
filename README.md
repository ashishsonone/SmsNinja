# SmsNinja
Securely share you OTP SMS for shared accounts/subscriptions

- It can selectively pick SMS (which match certain pattern - sender + body), then encrypt it using local key (`secretKey`) and publish it at URL (identified by `locationKey` e.g R4xZPuqC5)
- you share the `comboKey` = (`locationKey:secretKey`) with other party who you want to be able to access these messages
- They use the app, enter the combo key, and are able to decode the SMS published. We only keep the latest SMS at a paraticular `locationKey` (overwritten)
- Anybody can access the URL (if they know the `locationKey`), but only decrypt if they have the `secretKey` with them.

- Note: `secretKey` is a randomly generated 128 bit AES key (encoded using base64) e.g JbFVI1QnhPEGEfdg5oGm4A==
- Note: `locationKey` is a short id (7-14 charcters) generated by the server.
- Note: Only the owner device can publish content to `locationKey`, as it is tied to the owner's random `clientId` (UUID generated locally during first launch of app)
- Note: `secreteKey` is never sent to the server. It is stored locally on device. Only encrypted content is sent and stored at the `locationKey` url. And you're supposed to share the secret to your friend/family securely.
