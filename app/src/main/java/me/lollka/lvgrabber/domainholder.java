package me.lollka.lvgrabber;

/**
 * Created by lollka on 23.03.2017.
 */

public class domainholder{
    private String domainid;
    private String domain;

    public domainholder(String domainid, String domain){
        this.domainid = domainid;
        this.domain = domain;
    }

    public String getDomainid(){
        return this.domainid;
    }

    public String getDomain(){
        return this.domain;
    }

}
