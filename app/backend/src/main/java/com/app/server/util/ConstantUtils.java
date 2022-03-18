package com.app.server.util;

public class ConstantUtils {
	// Allow only letters and numbers
	public static final String USERNAME_PATTERN = "[a-zA-Z0-9]+";

	// Allow only letters, letters with special characters, numbers and spaces
	public static final String ADDRESS_PATTERN = "[a-zA-Z0-9-A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ,.ªº -]+";
	
	// Allow only letters, letters with special characters, numbers and spaces
	public static final String DESCRIPTION_PATTERN = "[a-zA-Z0-9-A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ,!?:.()$€ªº@_ -]+";

	// Allow only letters, letters with special characters and spaces
	public static final String CHAR_PATTERN = "[a-zA-Z-A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ ]+";

	// Allow only letters or letters with special characters
	public static final String ONLYCHAR_PATTERN = "[a-zA-Z-A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ]+";

	// Allow only numbers
	public static final String CODE_PATTERN = "[0-9]+";

	// Must contain 1 UpperCase, 1 LowerCase and 1 Number
	public static final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*$";
}
