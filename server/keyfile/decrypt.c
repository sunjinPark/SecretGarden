#include "header.h"

int SSU_SEC_aes_decrypt( uchar *enc_msg, int enc_msg_len, uchar *sk, int sk_len, uchar **msg, int *msg_len )
{
		// 입력 파라미터: uchar *enc_msg; int enc_msg_len; uchar *sk; int sk_len;
		// 출력 파라미터: uchar **msg; int *msg_len;
		// 내부변수
		int ret = TRUE;
		int tmplen;

		// EVP 객체를 이용한 AES 암호화
		// ctx(암호화에 사용되는 데이터들이 저장되는 임시 저장소) 초기화
		EVP_CIPHER_CTX ctx;
		EVP_CIPHER_CTX_init(&ctx);

		// 초기화: 암호 알고리즘 할당, 키 할당, IV 할당
		EVP_DecryptInit_ex(&ctx, EVP_des_ofb(), NULL, sk, IVseedConstant);

		// 초기화가 끝난후에 해야 한다. 복호문 저장할 버퍼 생성
		*msg = malloc( enc_msg_len );
		if( *msg == NULL )
				return FALSE;

		//업데이트, 마지막 블록을 제외 하고 모두 복호화
		if(!EVP_DecryptUpdate(&ctx, *msg, msg_len, enc_msg, enc_msg_len))
				ret = FALSE;

		// 종료. 마지막 블록을 복호화
		if(!EVP_DecryptFinal_ex(&ctx, *msg+(*msg_len), &tmplen))
				ret = FALSE;

		// 복호문 길이는 업데이트, 종료 과정에서 나온 결과의 합
		*msg_len += tmplen;

		EVP_CIPHER_CTX_cleanup(&ctx);

		return ret;
}
