package ping.pubsub.model;

public class ResponseInfo {
    private String data;
    public ResponseInfo(){

    }
    public ResponseInfo(String data){
        this.data = data;
    }
    public void setData(String data){
        this.data = data;
    }
    public String getData(){
        return data;
    }
}
