package epj2.model.user;

/**
 * Represents a domestic user in the vehicle rental simulation system.
 * This class extends {@link User} and sets the document type to ID CARD.
 * 
 * @author Jelena Maletić
 * @version 1.9.2024.
 */
public class DomesticUser extends User {
	
	/**
     * Constructs a new domestic user with the specified name.
     * The document type is set to ID_CARD.
     *
     * @param name the name of the domestic user.
     */
	public DomesticUser(String name) {
		super(name);
		documentType=DocumentType.ID_CARD;
	}
	
	/**
     * Returns the document type of the foreign user.
     *
     * @return the document type, which is Id card.
     */ 
	@Override
	public DocumentType getDocumentType() {
		return documentType;
	}
	
	/**
     * Creates and returns a clone of this domestic user.
     * The {@code CloneNotSupportedException} is caught and handled by throwing a 
     * {@code RuntimeException} with a message indicating that cloning is not supported. 
     * This ensures that any errors related to cloning are properly reported and handled, 
     * avoiding potential issues
     * @return a clone of this domestic user.
     */
    @Override
    public DomesticUser clone() {
        try {
            return (DomesticUser) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning failed", e);
        }
    }
}
