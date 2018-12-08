# zzq
springboot2+mybatis-plus3.0.5+mysql8持续更新中
### 获取token
grant_type:authorization_code
http://localhost:8666/oauth/authorize?response_type=code&client_id=client1&redirect_uri=https://www.baidu.com
http://localhost:8666/oauth/token?code=OFuqG8&client_id=client1&client_secret=123456&grant_type=authorization_code&redirect_uri=https://www.baidu.com
http://localhost:8666/admin/users?access_token=156bb618-3f3d-481e-aba4-71bdb7e711ce
grant_type:password
http://localhost:8666/oauth/token?client_id=client1&client_secret=123456&grant_type=password&username=admin&password=123456
grant_type:implicit
http://localhost:8666/oauth/authorize?response_type=token&client_id=client1&redirect_uri=https://www.baidu.com
grant_type:client_credentials
http://localhost:8666/oauth/token?client_id=client1&client_secret=123456&grant_type=client_credentials
