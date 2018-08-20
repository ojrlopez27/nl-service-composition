package edu.cmu.inmind.composition.apis;

import edu.cmu.inmind.composition.annotations.ArgDesc;
import edu.cmu.inmind.composition.annotations.Description;
import edu.cmu.inmind.composition.pojos.SocialNetworkPOJO;

/**
 * Created by oscarr on 8/17/18.
 */
public interface SocialNetworkService extends GenericService {
    @Description(capabilities = {
            "This method allows to change status on a given social network site",
            "This method allows to modify status on a social network site",
            "This method changes status on a given social network site",
            "This method modify status on a given social network site"
    })
    @ArgDesc(args = {
            "type of social network : which social network site?(facebook, instagram, snapchat, yelp)",
            "type of status : what type of status change? (checkin, text, photo, video)",
            "status : what is the status? (photo, text, video, checkin, tags, person tag )",
    })
    SocialNetworkPOJO changeNewStatus(String typeOfSocialNetwork, String typeOfStatus,
                         SocialNetworkPOJO snStatusPOJO);

    @Description(capabilities = {
            "This method allows to message a person on a given social network site",
            "This method allows to message a friend or eprson on a social network site",
            "This method messages to a another person on a given social network site",
            "This method messages to on a given social network site"
    })
    @ArgDesc(args = {
            "type of social network : which social network site?(facebook, instagram, snapchat, yelp)",
            "message: what is the message to be sent to ? (text)",
            "to : what is the person's name to send this message to",
    })
    SocialNetworkPOJO messageSomebody();

    @Description(capabilities = {
            "This method allows to upload a photo on a given social network site",
            "This method allows to send a photo on a social network site",
            "This method uploads a photo on a given social network site"
    })
    @ArgDesc(args = {
            "type of social network : which social network site?(facebook, instagram, snapchat, yelp)",
            "path of photo: what is the file path of photo to be uploaded ? (phone storage, click photo)",
            "upload type : what is the mode of upload? (upload as a status or post or as a review, or to a person"
    })
    SocialNetworkPOJO uploadPhoto(String typeOfSn, String filePath , String typeOfUpload);
}
