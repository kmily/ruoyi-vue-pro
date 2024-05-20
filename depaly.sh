#!/bin/bash

# Step 1: 拉取最新代码
echo "Step 1: 正在拉取最新代码..."
cd /home/www/hospital_mdd
git checkout dev
git pull
if [$? -ne 0 ]; then
  echo "拉取最新代码失败，请检查网络连接或者项目地址是否正确。"
  exit 1
fi
echo "最新代码拉取完成。"

# Step 2: 使用 Maven 打包项目
echo "Step 2: 正在打包项目..."
mvn clean
mvn package -Dmaven.test.skip=true
if [ $? -ne 0 ]; then
  echo "项目打包失败，请检查 Maven 配置和项目依赖。"
  exit 1
fi
echo "项目打包完成。"

# Step 3: 部署项目
echo "Step 3: 正在部署项目..."
deploy_dir="/home/www/hlg_back_service"
if [ ! -d "$deploy_dir" ]; then
  echo "部署目录 $deploy_dir 不存在，正在创建..."
  mkdir -p "$deploy_dir"
  if [ $? -ne 0 ]; then
    echo "创建部署目录失败，请检查权限和目录路径。"
    exit 1
  fi
fi

echo "正在复制项目到部署目录..."
cp -P -r /home/www/hospital_mdd/yudao-server/target/yudao-server.jar "$deploy_dir"
if [ $? -ne 0 ]; then
  echo "复制项目文件失败，请检查文件路径和权限。"
  exit 1
fi

echo "项目部署完成。"


# Step 3.5: 检查并关闭正在运行的 yudao-server.jar 服务（如果它正在运行）  
echo "Step 3.5: 检查并关闭正在运行的 yudao-server.jar 服务..."  
pid=$(pgrep -f "java.*yudao-server.jar")  
if [ -n "$pid" ]; then  
  echo "找到正在运行的 yudao-server.jar 进程，PID: $pid"  
  kill -9 $pid  
  if [ $? -eq 0 ]; then  
    echo "yudao-server.jar 进程已关闭。"  
  else  
    echo "关闭 yudao-server.jar 进程失败，请手动检查并关闭。"  
  fi  
else  
  echo "未找到正在运行的 yudao-server.jar 进程。"  
fi 


# Step 4: 启动项目
echo "Step 4: 正在启动项目..."
cd "$deploy_dir"
nohup java -jar yudao-server.jar > server.log 2>&1 &
echo "项目启动中..."

echo "部署完成。"