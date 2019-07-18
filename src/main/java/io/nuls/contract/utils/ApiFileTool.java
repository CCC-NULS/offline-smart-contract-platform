package io.nuls.contract.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.nuls.contract.rpc.resource.impl.OfflineContractResourceImpl;
import io.nuls.core.parse.JSONUtils;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @author: PierreLuo
 * @date: 2019-07-09
 */
public class ApiFileTool {

    static Set<Class> baseType = new HashSet<>();

    static {
        baseType.add(Integer.class);
        baseType.add(int.class);
        baseType.add(Long.class);
        baseType.add(long.class);
        baseType.add(Float.class);
        baseType.add(float.class);
        baseType.add(Double.class);
        baseType.add(double.class);
        baseType.add(Character.class);
        baseType.add(char.class);
        baseType.add(Short.class);
        baseType.add(short.class);
        baseType.add(Boolean.class);
        baseType.add(boolean.class);
        baseType.add(Byte.class);
        baseType.add(byte.class);
        baseType.add(String.class);
        baseType.add(Object[].class);
        baseType.add(BigInteger.class);
        baseType.add(BigDecimal.class);
    }

    public static class CmdDes implements Serializable {
        String cmdName;
        String cmdType;
        String des;
        String httpMethod;
        List<String> parameters;

        public String getCmdName() {
            return cmdName;
        }

        public void setCmdName(String cmdName) {
            this.cmdName = cmdName;
        }

        public String getCmdType() {
            return cmdType;
        }

        public void setCmdType(String cmdType) {
            this.cmdType = cmdType;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public String getHttpMethod() {
            return httpMethod;
        }

        public void setHttpMethod(String httpMethod) {
            this.httpMethod = httpMethod;
        }

        public List<String> getParameters() {
            return parameters;
        }

        public void setParameters(List<String> parameters) {
            this.parameters = parameters;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{");
            sb.append("\"cmdName\":")
                    .append('\"').append(cmdName).append('\"');
            sb.append(",\"des\":")
                    .append('\"').append(des).append('\"');
            sb.append('}');
            return sb.toString();
        }
    }

    public static void main(String[] args) throws IOException {
        Gen.genPostmanJSON();
        System.exit(0);
    }


    public static class Gen {

        private static final String NA = "N/A";
        private static final String JSONRPC = "JSONRPC";

        public static List<CmdDes> buildData() {
            Class<OfflineContractResourceImpl> clazs = OfflineContractResourceImpl.class;
            List<CmdDes>[] resultArray = new List[2];
            List<CmdDes> cmdDesList = new ArrayList<>();
            String cmdBaseName = "";
            String methodPath = "";
            Method[] methods = clazs.getDeclaredMethods();
            for (Method method : methods) {
                CmdDes cmdDes = new CmdDes();
                cmdDes.cmdType = JSONRPC;
                cmdDes.httpMethod = "POST";
                cmdDes.cmdName = method.getName();
                cmdDes.des = method.getName();
                cmdDes.parameters = buildParam(method);
                cmdDesList.add(cmdDes);
            }
            return cmdDesList;
        }

        private static List<String> buildParam(Method method) {
            List<String> result = new LinkedList<>();
            Parameter[] parameters = method.getParameters();
            for(Parameter parameter : parameters) {
                result.add(parameter.getName());
            }
            return result;
        }


        public static void genPostmanJSON() throws IOException {
            List<CmdDes> cmdDesList = buildData();
            String postmanFormatJson = genPostmanFormatJson(cmdDesList);
            System.out.println("生成Postman-JSONRPC导入文件成功：" + createPostmanJSONConfig(cmdDesList, "./readme.md"));
//            System.exit(0);
        }


        public static String createPostmanJSONConfig(List<CmdDes> cmdDesList, String tempFile) throws IOException {
            String appName = "offline-SC";
            File file = new File(tempFile);
            if (!file.exists()) {
                throw new RuntimeException("模板文件不存在");
            }
            File mdFile = new File(file.getParentFile().getAbsolutePath() + File.separator + "documents" + File.separator + appName + "_Postman_JSONPRC.json");
            if (mdFile.exists()) {
                mdFile.delete();
            }
            mdFile.createNewFile();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(mdFile))) {
                String postmanFormatJson = genPostmanFormatJson(cmdDesList);
                writer.write(postmanFormatJson);
            }
            return mdFile.getAbsolutePath();
        }



        public static String genPostmanFormatJson(List<CmdDes> cmdDesList) throws JsonProcessingException {
            PostmanFormat format = new PostmanFormat();
            Info info = new Info();
            format.setInfo(info);
            info.setName("Offline-SC");
            info.setSchema("https://schema.getpostman.com/json/collection/v2.1.0/collection.json");
            List<Item> item = new ArrayList<>();
            for(CmdDes des : cmdDesList) {
                Item aItem = new Item();
                aItem.setName(des.des + " - " + des.cmdName);
                Request request = new Request();
                Body body = new Body();
                Url url = new Url();
                request.setMethod(des.httpMethod);
                request.setBody(body);
                List<String> nameList = des.getParameters();
                body.setRaw(String.format(
                        "{\n\"jsonrpc\":\"2.0\",\n\"method\":\"%s\",\n\"params\":%s,\n\"id\":1234\n}\n",
                        des.cmdName,
                        nameList.toString()
                ));
                url = Url.jsonrpcInstance();
                request.setUrl(url);
                aItem.setRequest(request);
                item.add(aItem);
            }
            format.setItem(item);
            String formatStr = JSONUtils.obj2json(format);
            return formatStr;
        }

        private static class PostmanFormat {
            private Info info;
            private List<Item> item;

            public Info getInfo() {
                return info;
            }

            public void setInfo(Info info) {
                this.info = info;
            }

            public List<Item> getItem() {
                return item;
            }

            public void setItem(List<Item> item) {
                this.item = item;
            }
        }

        private static class Info {
            private String _postman_id;
            private String name;
            private String schema;

            public Info() {
                this._postman_id = UUID.randomUUID().toString();
            }

            public String get_postman_id() {
                return _postman_id;
            }

            public void set_postman_id(String _postman_id) {
                this._postman_id = _postman_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSchema() {
                return schema;
            }

            public void setSchema(String schema) {
                this.schema = schema;
            }
        }

        private static class Item {
            private String name;
            private Request request;
            private List<String> response;

            public Item() {
                this.response = new ArrayList<>();
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Request getRequest() {
                return request;
            }

            public void setRequest(Request request) {
                this.request = request;
            }

            public List<String> getResponse() {
                return response;
            }

            public void setResponse(List<String> response) {
                this.response = response;
            }
        }

        private static class Request {
            private String method;
            private List<Header> header;
            private Body body;
            private Url url;

            public Request() {
                this.header = new ArrayList<>();
                Header header = new Header();
                header.setKey("Content-Type");
                header.setName("Content-Type");
                header.setValue("application/json;charset=UTF-8");
                header.setType("text");
                this.header.add(header);
            }

            public String getMethod() {
                return method;
            }

            public void setMethod(String method) {
                this.method = method;
            }

            public List<Header> getHeader() {
                return header;
            }

            public void setHeader(List<Header> header) {
                this.header = header;
            }

            public Body getBody() {
                return body;
            }

            public void setBody(Body body) {
                this.body = body;
            }

            public Url getUrl() {
                return url;
            }

            public void setUrl(Url url) {
                this.url = url;
            }
        }

        private static class Header {
            private String key;
            private String name;
            private String value;
            private String type;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        private static class Body {
            private String mode;
            private String raw;

            public Body() {
                this.mode = "raw";
            }

            public String getMode() {
                return mode;
            }

            public void setMode(String mode) {
                this.mode = mode;
            }

            public String getRaw() {
                return raw;
            }

            public void setRaw(String raw) {
                this.raw = raw;
            }
        }

        private static class Url {
            private String raw;
            private String protocol;
            private List<String> host;
            private String port;
            private List<String> path;

            public Url() {
                this.protocol = "http";
                this.host = new ArrayList<>();
                this.host.add("localhost");
                this.port = "80";
                this.path = new ArrayList<>();
            }

            public static Url jsonrpcInstance() {
                Url url = new Url();
                url.path.add("jsonrpc");
                url.raw = "http://localhost/offlineSmartContract";
                return url;
            }

            public String getRaw() {
                return raw;
            }

            public void setRaw(String raw) {
                this.raw = raw;
            }

            public String getProtocol() {
                return protocol;
            }

            public void setProtocol(String protocol) {
                this.protocol = protocol;
            }

            public List<String> getHost() {
                return host;
            }

            public void setHost(List<String> host) {
                this.host = host;
            }

            public String getPort() {
                return port;
            }

            public void setPort(String port) {
                this.port = port;
            }

            public List<String> getPath() {
                return path;
            }

            public void setPath(List<String> path) {
                this.path = path;
            }
        }
    }

}
