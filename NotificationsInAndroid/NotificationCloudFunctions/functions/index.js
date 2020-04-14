const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();

exports.onNodeAdded = functions.database.ref('/notifications/{notificationID}')
    .onCreate((snapshot, context) => {
        const data = snapshot.val();
        var deviceToken = `${data.token}`;
        var title = `${data.title}`
        var body = `${data.body}`

        var payload = {
            "notification": {
                "title": title,
                "body": body,
                "sound": "default",
                "image": "https://firebasestorage.googleapis.com/v0/b/notifications-f07f0.appspot.com/o/notificationImage.jpg?alt=media&token=0c552faa-6dc3-46d6-a2dd-8b30b04ff74a"
            }
        };
        return admin.messaging().sendToDevice([deviceToken], payload).then((response) => {
            return console.log('Notification pushed');
        });
    });