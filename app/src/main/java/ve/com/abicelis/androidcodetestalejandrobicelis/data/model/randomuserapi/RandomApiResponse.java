package ve.com.abicelis.androidcodetestalejandrobicelis.data.model.randomuserapi;

import java.util.List;

/**
 * Created by abicelis on 9/9/2017.
 */

public class RandomApiResponse {

    private List<RandomApiResult> results;
    public List<RandomApiResult> getResults() {return results;}



    public class RandomApiResult {
        private RandomApiName name;
        private RandomApiLocation location;
        private String email;
        private RandomApiDob dob;
        private String phone;
        private String cell;
        private RandomApiPicture picture;

        public RandomApiName getName() {return name;}
        public RandomApiLocation getLocation() {return location;}
        public String getEmail() {return email;}
        public RandomApiDob getDob() {return dob;}
        public String getPhone() {return phone;}
        public String getCell() {return cell;}
        public RandomApiPicture getPicture() {return picture;}
    }




    public class RandomApiName {
        private String first;
        private String last;

        public String getFirst() {return first;}
        public String getLast() {return last;}
    }

    public class RandomApiLocation {
        private String street;
        private String city;
        private String state;
        private String postcode;

        public String getStreet() {return street;}
        public String getCity() {return city;}
        public String getState() {return state;}
        public String getPostCode() {return postcode;}
    }

    public class RandomApiDob {
        private String date;
        private String age;

        public String getDate() {return date;}
        public String getAge() {return age;}
    }

    public class RandomApiPicture {
        private String large;

        public String getLarge() {return large;}
    }
}
