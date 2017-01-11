package pl.szleperm.examples.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Customer {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@NotBlank
	@Size(min=3, max=150)
	@Column(nullable=false)
	private String name;
	
	@NotBlank
	@Pattern(regexp="[0-9]{10}", message="must be 10 digit number")
	@Column(name="nip_number", nullable=false)
	private String nipNumber;
	
	@NotBlank
	@Size(min=3, max=150)
	@Column(nullable=false)
	private String address;

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNipNumber() {
		return nipNumber;
	}

	public void setNipNumber(String nipNumber) {
		this.nipNumber = nipNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public Customer(){
		super();
	}

	public Customer(Long id, String name, String nipNumber, String address) {
		super();
		this.id = id;
		this.name = name;
		this.nipNumber = nipNumber;
		this.address = address;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Customer [name=" + name + ", nipNumber=" + nipNumber + ", adress=" + address + "]";
	}
	
}
