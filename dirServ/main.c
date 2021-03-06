#include <stdio.h>  
#include <stdlib.h>  
#include <string.h>
#include <signal.h> 
#include <sys/epoll.h>
#include <mysql.h>

#include "dirs.h"
#include "protocol.h"

#define MAXLINE 1024


int main(int argc, char **argv)
{
   byte headerBuf[HEADERSIZE];
   byte dataBuf[DATASIZE];
   char directoryList[DATASIZE];
   char *tokenBuf[DATASIZE];
   
   char *id[100];
   char *pwd[100];
   int   state = 1;
   MYSQL *con;   

   char *String = "OK";
   char *NoneMs = "Error";
   int type = 0;
   int desc = 0;
   int length = 0;
   Peer messagingServ;  

 
  
   messagingServ.socket =  joinToPassingServ("127.0.0.1", "12500", 102);	
   
        // DB connection 초기화
	con = mysql_init(NULL);
	if(con == NULL) 
	{	
		fprintf(stderr,"%s\n",mysql_error(con));
	}
		
        // DB 연결
	if(mysql_real_connect
             (con,"localhost","root","32sm2u","SecretGarden", 0, NULL, 0) == NULL)
 	 {
		fprintf(stderr,"%s\n",mysql_error(con));
         }
	
	// DB 인코딩
	mysql_query(con, "set session character_set_connection=utf8");
	mysql_query(con, "set session character_set_results=utf8");
	mysql_query(con, "set session character_set_client=utf8");
   
   while(1) 
   {
	memset(headerBuf, 0x00, HEADERSIZE);
	memset(dataBuf,   0x00, DATASIZE);
	memset(tokenBuf,  0x00, DATASIZE);
	memset(directoryList, 0x00, DATASIZE);

	int recv_len =  recvFrom(&messagingServ, headerBuf, dataBuf);
	
	/* Send */
	if(recv_len > 0)
	{
			
		type = byteToInt(headerBuf, 0);
		desc = byteToInt(headerBuf, 4);
		length = byteToInt(headerBuf, 8);
		
		switch(type)
		{
			case 7 :
			{
			
			     // 디렉토리 조회를 위하여 폴더이름으로 조회
			     getElements(&dataBuf, '\t', tokenBuf);	
			 
			     // User가 가진 Haddop 
	     		     // User id, Public, Private
			     state = getdirectoryList(con, "Test", "hadoop","ktclout",directoryList);
				
			     if(state == 1)
			     {
        		    	 sendTo(&messagingServ, 8,
                             		    desc, strlen(directoryList), directoryList);
			     }
			     else
			     {
				 sendTo(&messagingServ, 0,
                             		    desc, strlen(NoneMs), NoneMs);
			     }
                             break;
			}
			case 9 :
			{
			     // 디렉토리 생성
			     getElements(dataBuf, '\t', tokenBuf);
			     state = createRootDir(con, tokenBuf[0], "hadoop", "ktclout", "Test");
			     //createRootDir(con, "mytest", "hadoop", "ktclout", "Test");
			     //state = createDirectory(con, tokenBuf[0],tokenBuf[1],tokenBuf[2],tokenBuf[3],tokenBuf[4],tokenBuf[5]);

			     if(state == 1)
			     {
			    	sendTo(&messagingServ, 10, 
				    desc, strlen(String), String);
			     }

			     else
			     {
				sendTo(&messagingServ, 0, 
				    desc, strlen(NoneMs), NoneMs);
			     }

			}
			// 디렉토리 엑세스
			// 하위 폴더 생성
			default  :
			{
			     break;
			}
		}
	
	}
	/* Send End */

   }
}

