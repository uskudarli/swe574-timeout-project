package tr.edu.boun.swe574.timeoutclient.jsonObjects;

/**
 * Created by haluks on 25/05/15.
 */
public class FindReturnObject {


    public class User {
        Integer userId;
        String userEmail;
        String date;
        String password;

        public class role {
            Integer roleId;
            String name;
        }

        public class userBasicInfo {
            Integer userId;
            String firstName;
            String lastName;
            String gender;
        }

    }

    public class Action {
        /*
                {
            "actionId": 41,
            "title": "Oğuz Sevenler",
            "description": "Oğuzu seven insanlar birliğidir, burada buluşacak ve oğuzu gıdıklayacaklardır.",
            "createTime": 1432393947000,
            "privacy": "C",
            "actionType": "G",
            "startTime": 1432393947000,
            "endTime": null
        }
         */

        Integer actionId;
        String title;
        String description;
        String createTime;
        String privacy;
        String actionType;
        String startTime;
        String endTime;
    }

    /*
    {
    "users": [
        {
            "userId": 60,
            "userEmail": "oguzcam_50@hotmail.com",
            "date": null,
            "password": "0aa0127aae3c911130d58ae733919447",
            "role": {
                "roleId": 1,
                "name": "student"
            },
            "userBasicInfo": {
                "userId": 60,
                "firstName": "OĞUZ",
                "lastName": "ÇAM",
                "gender": null
            },
            "userCommInfo": {
                "userId": 60,
                "mobilePhone": 0,
                "address": null
            },
            "userExtraInfo": {
                "userId": 60,
                "birthDate": null,
                "about": null,
                "interests": null,
                "languages": null
            }
        }
    ],
    "events": [
        {
            "actionId": 41,
            "title": "Oğuz Sevenler",
            "description": "Oğuzu seven insanlar birliğidir, burada buluşacak ve oğuzu gıdıklayacaklardır.",
            "createTime": 1432393947000,
            "privacy": "C",
            "actionType": "G",
            "startTime": 1432393947000,
            "endTime": null
        }
    ],
    "groups": [
        {
            "actionId": 41,
            "title": "Oğuz Sevenler",
            "description": "Oğuzu seven insanlar birliğidir, burada buluşacak ve oğuzu gıdıklayacaklardır.",
            "createTime": 1432393947000,
            "privacy": "C",
            "actionType": "G",
            "startTime": 1432393947000,
            "endTime": null
        }
    ]
}
     */
}
