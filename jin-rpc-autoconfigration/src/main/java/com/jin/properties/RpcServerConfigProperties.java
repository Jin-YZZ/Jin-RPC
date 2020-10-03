package com.jin.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * creat by Jin 2020/8/18 10:30
 *
 * @Description:
 */
@ConfigurationProperties(prefix = "rpc.server")
@Data
@NoArgsConstructor
public class RpcServerConfigProperties {
    private String host="127.0.0.1";
    private int port;
    private  int timeout=3000;
    private double weight=1.0;
    private String clusterName ="RPC-Cluster" ;
    @Value("${rpc.server.rpcServername:${spring.application.name:}}")
    private  String rpcServername;
    private Integer bossgroupnums=1;
    //private Integer threadLong=128;
    private Integer threadLong=128;
    private Boolean keepalive=true;
    private String clientpackages;
}
