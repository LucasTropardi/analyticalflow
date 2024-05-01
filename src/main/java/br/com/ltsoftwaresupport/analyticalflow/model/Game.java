package br.com.ltsoftwaresupport.analyticalflow.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "releasedate")
    private LocalDate releaseDate;

    @Column(name = "website")
    private String website;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @ElementCollection(targetClass = Platform.class)
	@JoinTable(name = "game_platform", joinColumns = @JoinColumn(name = "game_id", foreignKey = @ForeignKey(name = "fk_game_platform")))
	@Column(name = "platform", nullable = false)
	@Enumerated(EnumType.STRING)
	private Set<Platform> platform = new HashSet<>();

    @Lob
    @Column(name = "image", nullable = false, columnDefinition = "mediumblob")
    private byte[] image;

    @Transient
    private double averageRating;

    public Game() {

    }

    public Game(long id, @NotBlank String name, @NotNull LocalDate releaseDate, String website, Publisher publisher,
                Set<Platform> platform, byte[] image) {
        super();
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.website = website;
        this.publisher = publisher;
        this.platform = platform;
        this.image = image;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Set<Platform> getPlatform() {
        return platform;
    }

    public void setPlatform(Set<Platform> platform) {
        this.platform = platform;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
    
    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", releaseDate=" + releaseDate +
                ", website='" + website + '\'' +
                ", publisher=" + publisher +
                ", platform=" + platform +
                ", image=" + image +
                '}';
    }
}