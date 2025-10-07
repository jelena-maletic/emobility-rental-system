package epj2.model.user;

/**
 * Represents a foreign user in the electric vehicle rental simulation system.
 * This class extends {@link User} and sets the document type to passport.
 * 
 * @author Jelena Maletić
 * @version 1.9.2024.
 */
public class ForeignUser extends User {

	 /**
     * Constructs a new foreign user with the specified name.
     * The document type is set to PASSPORT
     *
     * @param name the name of the foreign user.
     */
	public ForeignUser(String name) {
		super(name);
		documentType=DocumentType.PASSPORT;
	}
	
	/**
     * Returns the document type of the foreign user.
     *
     * @return the document type, which is passport.
     */
	@Override
	public DocumentType getDocumentType() {
		return documentType;
	}
	
    /**
     * Creates and returns a clone of this foreign user.
     * The {@code CloneNotSupportedException} is caught and handled by throwing a 
     * {@code RuntimeException} with a message indicating that cloning is not supported. 
     * This ensures that any errors related to cloning are properly reported and handled, 
     * avoiding potential issues
     *
     * @return a clone of this foreign user.
     */
    @Override
    public ForeignUser clone() {
        try {
            return (ForeignUser) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning failed", e);
        }
    }
}
