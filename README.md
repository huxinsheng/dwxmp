# dwxmp
1、服务层可以用web容器部署，也可以用Main方法运行

2、打包请参考clean package -P dev

3、nginx配置

server {

    listen       80;
    
    server_name  localhost  alias  localhost.alias;

    #charset koi8-r;

    #access_log  logs/host.access.log  main;

    location ~ .*\.(gif|jpg|png|htm|html|css|js|flv|ico|swf|woff|tff)(.*) {
        root E:/devwork/dwxmp/dwxmp-ui/angular-ui;
        index  index.html index.htm index.jsp;
    }
    location / {
        proxy_pass http://127.0.0.1:8088;
    }
}

4、用户名密码:admin/111111

5、http://localhost/进入页面