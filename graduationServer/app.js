const fs=require('fs');
const express=require('express');
const app=express();
const port = process.env.PORT || 3000
const path=require('path');
const ejs=require('ejs');
const mainRouter=require('./router/main.js');
app.use('/js',express.static(__dirname+'/node_modules/bootstrap/dist/js'));
app.use('/css',express.static(__dirname+'/node_modules/bootstrap/dist/css'));
app.use('/',mainRouter);
app.use(express.json());
app.use(express.urlencoded());
app.use(express.static(__dirname+'/'));
app.set('views',path.join(__dirname,'views'));
app.set("view engine","ejs");

app.listen(port,()=>{
    console.log('server start!');
});