<template>
  <div>
    <el-card style="width: 40%; margin-left: 120px; margin-top: 40px" >
        <h2 style="padding: 30px">个人信息</h2>
      <el-form :model="form" ref="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input style="width: 80%" v-model="form.username" disabled></el-input>
        </el-form-item>
        <el-form-item label="身份证号">
          <span style="width: 80%" >{{form.num}}</span>
        </el-form-item>
        <el-form-item label="权限">
            <span v-if="form.role==1" style="margin:5px">管理员</span>
            <span v-if="form.role==2" style="margin:5px">读者</span>
        </el-form-item>
        <el-form-item label="电话号码">
          <el-input style="width: 80%" v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <div>
            <el-radio v-model="form.sex" label="男">男</el-radio>
            <el-radio v-model="form.sex" label="女">女</el-radio>
          </div>
        </el-form-item>
        <el-form-item label="地址">
          <el-input type="textarea" style="width: 80%" v-model="form.address"></el-input>
        </el-form-item>
        <el-form-item >
            <el-upload
              class="avatar-uploader"
              accept="image/*"
              :action="uploadFileUrl"
              :show-file-list="false"
              :on-success="handleUploadSuccess"
              :before-upload="handleBeforeUpload"
              
            >
            
              <img v-if="avatarUrl" :src="avatarUrl" class="avatar" />
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
              
            </el-upload>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleRemove">取消</el-button>
          </el-form-item>
      </el-form>
      <div style="text-align: center">
        <el-button type="primary" @click="update">保存</el-button>
      </div>
    </el-card>
    <el-dialog v-model="dialogVisible">
        <img w-full :src="dialogImageUrl" style="width: 500px" alt="预览图片" />
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";
import {ElMessage} from "element-plus";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Person",
  data() {
    const validatePhone = (rule, value, callback) => {
        if (value === "") {
          callback(new Error("请输入手机号码"));
        } else {
          //正则表达式，校验密码规则
          const reg = /^[1][3,5,6,7,8,9][0-9]{9}$/;
          if (!reg.test(value)) callback(new Error("请输入正确的手机号码"));
          else callback();
        }
    };
    return {
      form: {},
      editVisible: false,
      rules: {phone: [
            { required: false, message: "请输入手机号码", trigger: "blur" },
            {
              validator: validatePhone,
              trigger: "change",
            },
          ],
        },
        //文件上传url
        uploadFileUrl: "/api/file/upload",
        //文件访问url前缀
        downloadFileUrl: "/api/file/download?fileId=",
        //头像图片url
        
        avatarUrl: undefined,
        //放大显示图片url
        dialogImageUrl: "",
        //放大显示图片dialog显示属性
        dialogVisible: false,
        //多文件上传文件列表数据
        fileList: [],
        //是否有新文件上传
        hasNewFile: false,
        //是否自动上传文件
        autoUpload: false,
    }
  },
  created() {
    let str = sessionStorage.getItem("user") || "{}"
    this.form = JSON.parse(str)
    if (this.form.pictureid) {
    this.avatarUrl = this.downloadFileUrl + this.form.pictureid;
    this.dialogImageUrl = this.avatarUrl;
  }
    
  },
  methods: {
    update() {
      request.put("/user", this.form).then(res => {
        
        if (res.code === '0') {
          ElMessage.success("更新成功")
          this.editVisible = false
          sessionStorage.setItem("user", JSON.stringify(this.form))
          // 触发Layout更新用户信息
          this.$emit("userInfo")
        } else {
          ElMessage.error(res.msg)
        }
      })

    },
    
    //单文件上传，选择文件直接上传后返回
      // eslint-disable-next-line no-unused-vars
      handleUploadSuccess(res, file) {
        this.avatarUrl = this.downloadFileUrl + res.data.id;
        this.form.avatarUrl = this.avatarUrl;
        this.form.pictureid=res.data.id
        //if (!this.autoUpload) {
          //非自动上传的组件，在上传文件预处理完成后，提交save请求
          //this.update();
        
      },
      //单文件上传，选择文件直接上传前钩子事件
      handleBeforeUpload(file) {
        if (!file.type.startsWith("image/")) {
          this.$message.warning("请上传图片");
          return false;
        }
        //判断文件大小
        if (file.size / 1024 / 1024 > 1) {
          this.$message.warning("上传头像图片大小不能超过1 MB!");
          return false;
        }
      },
      
      handleRemove() {
        this.fileList = [];
        this.dialogImageUrl = "";
        this.form.avatarUrl = "";
        this.form.pictureid=""
        this.avatarUrl='';
      },
  }
}
</script>

<style>
.avatar-uploader  {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader:hover {
  border-color: #409EFF;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
.box-card {
  width: 60%;
  margin: auto;
  padding: 20px;
}
</style>
<style>
  .avatar-uploader .avatar {
    width: 100px;
    height: 100px;
    display: block;
  }
  .avatar-uploader .el-upload {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
  }
  
  .avatar-uploader .el-upload:hover {
    border-color: var(--el-color-primary);
  }
  
  .el-icon.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 100px;
    height: 100px;
    text-align: center;
  }
  
  </style>
  