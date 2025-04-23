package lab4.api.rest;

import jakarta.ws.rs.WebApplicationException;

public class ImageServer implements RestImage{
    @Override
    public String createImage(byte[] imageContents) {
        return "";
    }

    @Override
    public byte[] getImage(String imageId) {
        throw new WebApplicationException(errorCodeToStatus());
    }

    @Override
    public void deleteImage(String imageId) {

    }
}
