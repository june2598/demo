--replyproduct 테이블 삭제
drop table replyProduct;
--reply_id 시퀀스 삭제
drop sequence replyproduct_reply_id_seq;

--replyproduct 테이블 생성
CREATE TABLE ReplyProduct (
    reply_id NUMBER(10) PRIMARY KEY,
    product_id NUMBER(10),
    comments CLOB,
    writer VARCHAR2(30),
    cdate TIMESTAMP,
    udate TIMESTAMP,
    FOREIGN KEY(product_id) REFERENCES product(product_id)
);

--reply_id 시퀀스 생성
create sequence replyproduct_reply_id_seq;

insert into replyproduct (reply_id,product_id,comments,writer,cdate)
            values(replyproduct_reply_id_seq.nextval,2,'댓글내용1','작성자1',sysdate);
insert into replyproduct (reply_id,product_id,comments,writer,cdate)
            values(replyproduct_reply_id_seq.nextval,2,'댓글내용2','작성자2',sysdate);
insert into replyproduct (reply_id,product_id,comments,writer,cdate)
            values(replyproduct_reply_id_seq.nextval,401,'댓글내용3','작성자3',sysdate);
insert into replyproduct (reply_id,product_id,comments,writer,cdate)
            values(replyproduct_reply_id_seq.nextval,401,'댓글내용4','작성자4',sysdate);

commit;

--조회
select *
from replyproduct;

select *
from product;

--수정
set comments = '내용2', udate = sysdate
where reply_id = 1;



