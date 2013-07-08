#include "auth.h"

int 
auth(MYSQL *con, char *id, char *pwd)
{
	int type;
	int length;
	byte dataBuf[DATASIZE];

	char query[255];
	MYSQL_ROW row;
		
	sprintf(query,"SELECT * FROM User WHERE user_id='%s' && pwd='%s'",id,pwd);
	printf("query is %s \n", query);

	if (mysql_query(con, query)) {
			fprintf(stderr,"%s\n",mysql_error(con));
			strcpy(dataBuf,mysql_error(con));
			return 0;
	}

	MYSQL_RES *result = mysql_store_result(con);

	if (result == NULL) {
			
			fprintf(stderr,"%s\n",mysql_error(con));
			return 0;
	}

	int num_fields = mysql_num_fields(result);

	row = mysql_fetch_row(result);

	if(row == NULL){
			
			strcpy(dataBuf,"Incorrect ID or Password");
			length = strlen(dataBuf);
			return 0;
	}
	else {
			
			strcpy(dataBuf,"");
			return 1;
	}


	return 1;
}


int 
signUp(MYSQL *con, char *id, char *pwd, char *name, char *email)
{
	char query[255];
	byte dataBuf[DATASIZE];
	int type;
	int length;
	printf("id : %s, pwd : %s, name : %s, email : %s \n", id, pwd, name, email);
	sprintf(query,"INSERT INTO SecretGarden.User(user_id, pwd, name, email) VALUES ('%s', '%s', '%s', '%s')",id,pwd,name,email);

	if (mysql_query(con, query)) 
	{
			fprintf(stderr,"%s\n",mysql_error(con));
			strcpy(dataBuf,"This ID is already taken");
			return 0;
	}
	else{
			strcpy(dataBuf,"");
			return 1;
	}

	return 1;
}


int 
getElements(byte *dataBuf, char *token, char *tokenBuf[])
{
	printf("getElement 진입 \n");
	int   i=0;
	char *str;
	
	str = dataBuf;
	str = strtok(str, "\t");	

	printf("strtok : %s \n", str); 
	while(str != NULL)
	{	
		tokenBuf[i++] = str;
		printf("token %d : %s \n", i-1, tokenBuf[i-1]);
		str = strtok(NULL, "\t");	
		//printf("strtok : %s \n", token); 
	}
	printf("strtok 2 \n");
        
	return i;	
}








