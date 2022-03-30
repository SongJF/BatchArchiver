# Intro
- 提供批量压缩能力的安卓端APP

- [下载链接](https://github.com/SongJF/BatchArchiver/releases)

# 介绍

## 批量压缩
- 操作流程
    - 选择源路径
    - 选择目标路径
    - 选择压缩级别
    - 设置密码(可选)
    - 执行压缩
    
    源路径下的所有子文件夹被单独作为一个压缩文档归档至目标路径

- 错误信息

    压缩失败的路径将信息会将会被输出到剪贴板

## 据sqlite数据批量压缩(高级)
- 操作流程
    - 选择源路径
    - 选择sqlite db文件
    - 输入sql语句，若已在sql页签添加过sql语句，输入时会自动补全(需select出src和dest(可选)， src决定压缩哪些文件夹，dest决定归档文件名(若未select出则为文件夹原名))
    - 选择目标路径
    - 选择压缩级别
    - 设置密码(可选)
    - 执行压缩
    
    源路径下的所有匹配的子文件夹被单独作为一个压缩文档归档至目标路径

- 错误信息

    压缩失败的路径将信息会将会被输出到剪贴板

## 保存sql语句
- 添加sql语句
    
    切换至sql页签，点击加号，输入sql语句

- 删除sql语句

    长按以删除sql语句

## 致谢
本项目的开发得益于于以下开源项目

[fastjson](https://github.com/alibaba/fastjson/)

[zip4j](https://github.com/srikanth-lingala/zip4j)

[commons-compress](https://commons.apache.org/proper/commons-compress/)

[AndroidPicker](https://github.com/gzu-liyujiang/AndroidPicker)

[VerticalStepperForm](https://github.com/ernestoyaquello/VerticalStepperForm)