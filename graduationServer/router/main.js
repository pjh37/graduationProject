const express=require('express');
const fs=require('fs');
const path=require('path');
const multiparty=require('multiparty');
const mkdirp=require('mkdirp');
const mysql=require('mysql');
const router=express.Router();
var connection=mysql.createConnection({
    host : 'jjjj1352.cafe24.com',
    user : 'jjjj1352',
    password : 'qkrwlgy37@',
    datebase : 'jjjj1352',
    charset: 'utf8_bin'
});
connection.connect(function(err){
    if(err){
        console.log('connect error');
    }else{
        console.log('mysql connected!!');
    }
});
router.get('/',function(req,res){
    //테스트
    var _id=1;
    var title;
    var description;
    var json;
    var query='SELECT * FROM jjjj1352.form WHERE _id=?';
        var params=[_id];
        connection.query(query,params,function(err,rows,fields){
            if(err){
                console.log("데이터 select 오류");
            }else{
                console.log("데이터 select 성공");
                title=rows[0].title;
                description=rows[0].description;
                json=rows[0].json;
                res.render('main',{
                    title:title,
                    name:'page',
                    description:description,
                    json:json
                });
            }
            
        });


    //
   
});
router.post('/save',function(req,res,next){
    var form=new multiparty.Form();
    var userID;
    var json;
    var title;
    var description;
    var time;
    form.parse(req);
    form.on('field',function(name,value){
        console.log('filed 들어옴');
        if(name=='json'){
            json=value;
            var jsonObject=JSON.parse(json);
            userID=jsonObject.userID;
            title=jsonObject.title;
            description=jsonObject.description;
            time=jsonObject.time;
            console.log(json);
        }
    });
    //보낸 사람, 설문지 id, 
    form.on('close',function(){
        var query='INSERT INTO jjjj1352.form (user_id,title,description,json,time) VALUES(?,?,?,?,?)';
        var params=[userID,title,description,json,time];
        connection.query(query,params,function(err,rows,fields){
            if(err){
                console.log('저장실패'+err);
            }else{
                console.log('저장완료');
                res.status(200).send('Upload complete');
            }
        });
        
    });
});
router.post('/user/form',function(req,res,next){
    var userID;
    var json;
    var response_cnt;
    var form=new multiparty.Form();
    form.parse(req);
    form.on('field',function(name,value){
        console.log('filed 들어옴');
        if(name=='userID'){
           
            
            userID=value;
            console.log(json);
        }
    });
    form.on('close',function(){
        var query='SELECT json,response_cnt,time FROM jjjj1352.form WHERE user_id=?';
        var params=[userID];
        connection.query(query,params,function(err,rows,fields){
            if(err){
                console.log("데이터 select 오류");
            }else{
                console.log("데이터 select 성공");
                var jsonObject=new Array();
                for(i=0;i<rows.length;i++){
                    var temp=new Object();
                    
                    temp.json= JSON.parse(rows[i].json);
                    temp.response_cnt=rows[i].response_cnt;
                    var buf=new Buffer(rows[i].time,'base64');
                    temp.time=buf.toString();
                    
                    jsonObject.push(temp);
                }
                console.log(jsonObject[0]);
                return res.json(jsonObject);
            }
            
        });
    });
   
});
module.exports=router;