function firebase_setup(registration, config, vapidKey, showNotificationOptIn) {
    if (!firebase.messaging.isSupported()){
        console.log("Browser doesn't support WebPush");
        return
    }

    console.log('Initializing Firebase');
    firebase.initializeApp(config);

    const messaging = firebase.messaging();
    messaging.usePublicVapidKey(vapidKey);
    messaging.useServiceWorker(registration);

    // Get permission from user
    firebase_request_permission(registration, messaging, showNotificationOptIn);
}

function firebase_request_permission(registration, messaging, showNotificationOptIn) {
    if (!showNotificationOptIn || Notification.permission == 'denied') {
        return;
    }

    $('#push-container .btn-allow').click(function(){
        try {
            messaging.requestPermission();
            wutsi.track('pwapush');

            // Get token
            firebase_get_token(messaging);

            // Refetsh token
            messaging.onTokenRefresh(function()  {
                firebase_get_token(messaging);
            });
        } catch (e) {
            console.log('Unable to get permission', e);
            return;
        } finally {
            $('#push-container').hide();
        }
    });

    registration.pushManager.getSubscription()
        .then(function(subscription) {
            console.log('User subscription', subscription);
            if (subscription === null){
                console.log('Show Notification OptIn panel');
                $('#push-container').show();
            }

            // Refetsh token
            messaging.onTokenRefresh(function()  {
                firebase_get_token(messaging);
            });
        });
}

function firebase_get_token(messaging) {
    messaging.getToken()
        .then(function(token){
            console.log('Token Refreshed', token);
            wutsi.httpPost('/push/subscription', { token: token }, true);
        });
}
