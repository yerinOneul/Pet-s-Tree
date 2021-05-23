package kr.ac.gachon.sw.petstree.animal;

//유기동물 recylerview에 표시될 정보를 저장하는 class
//image : 유기동물 사진
//age, sex, weight, kind : 나이, 성별, 체중, 품종
//place, shelter : 발견 장소, 보호소

public class Animal {
    private String image,age,sex,weight,kind,place,shelter;

    public void setImage(String image){
        this.image = image;
    }
    public String getImage(){
        return image;
    }
    public void setAge(String age){
        this.age = age;
    }
    public String getAge(){
        return age;
    }
    public void setSex(String sex){
        this.sex = sex;
    }
    public String getSex(){
        return sex;
    }
    public void setWeight(String weight){
        this.weight = weight;
    }
    public String getWeight(){
        return weight;
    }
    public void setKind(String kind){
        this.kind = kind;
    }
    public String getKind(){
        return kind;
    }
    public void setPlace(String place){
        this.place = place;
    }
    public String getPlace(){
        return place;
    }
    public void setShelter(String shelter){
        this.shelter = shelter;
    }
    public String getShelter(){
        return shelter;
    }


}
