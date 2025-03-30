package lab3.server.resources;

import java.util.List;
import java.util.logging.Logger;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import lab3.api.User;
import lab3.api.service.RestUsers;
import lab3.server.persistence.Hibernate;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lab3.api.service.RestImage;

import java.io.*;
import java.util.Arrays;
import java.util.UUID;

public class ImageResource implements RestImage {

    private static Logger Log = Logger.getLogger(UsersResource.class.getName());
    private static Hibernate hibernate = Hibernate.getInstance();

    @Override
    public String createImage(byte[] imageContents) throws IOException {
        if (imageContents.length == 0) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }
        boolean fileValid= false;
        File newImage = null;
        UUID newImageId = UUID.randomUUID();
        while(!fileValid){
            File temp = new File("imageFiles/" + newImageId.toString() + ".png");
            fileValid = !temp.exists();
        }

        newImage = new File("imageFiles/" + newImageId.toString() + ".png");
        BufferedWriter bf = new BufferedWriter(new FileWriter(newImage));
        bf.write(Arrays.toString(imageContents));
        bf.close();

        return RestImage.PATH + "/" + newImageId.toString();
    }

    @Override
    public byte[] getImage(String imageId) {
        File imageFile = new File("imageFiles/" + imageId + ".png");
        if (!imageFile.exists()) {
            Log.info("Image file " + imageId + " not found");
            throw new WebApplicationException(Status.NOT_FOUND);
        }
            
    }

    @Override
    public void deleteImage(String imageId) {
        hibernate.delete(imageId + ".png");
    }
}
