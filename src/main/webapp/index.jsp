<html>
    <head>
        <%@page contentType="text/html; UTF-8" pageEncoding="UTF-8" %>
        <%--<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />--%>
    </head>
    <body>
        <h2>Hello World!</h2>
        <form action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
            <input type="file" name="upload_file" />
            <input type="submit" value="上传文件" />
        </form>

        <form action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
            <input type="file" name="upload_file" />
            <input type="submit" value="上传文件" />
        </form>
    </body>
</html>
