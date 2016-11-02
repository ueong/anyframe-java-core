/*
 * Copyright 2008-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.anyframe.plugin.mobile.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

/**
 * This Movie class is a Value Object class for Movie domain.
 * 
 * @author Jongpil Park
 */
@Entity
public class Movie implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
	private String movieId;

	@NotNull
	@Size(min = 1, max = 50)
	private String title = "";

	@NotNull
	@Size(min = 1, max = 50)
	private String director;

	private Genre genre;

	@NotNull
	@Size(min = 5, max = 100)
	private String actors;

	@DecimalMax(value = "180")
	private Long runtime;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Past
	private Date releaseDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Past
	private Date startDate;
	

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Past
	private Date endDate;

	//Velocity-Support-ticketPrice-START
	@NumberFormat(pattern = "#,###")
	@DecimalMin(value = "1")
	@Digits(integer = 4, fraction = 0)
	private Float ticketPrice;
	//Velocity-Support-ticketPrice-END

	private String posterFile;

	private String nowPlaying = "Y";

	public Movie() {
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public String getMovieId() {
		return movieId;
	}

	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Float getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(Float ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public String getActors() {
		return actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	public Long getRuntime() {
		return runtime;
	}

	public void setRuntime(Long runtime) {
		this.runtime = runtime;
	}

	public String getNowPlaying() {
		return nowPlaying;
	}

	public void setNowPlaying(String nowPlaying) {
		this.nowPlaying = nowPlaying;
	}

	public String getPosterFile() {
		return posterFile;
	}

	public void setPosterFile(String posterFile) {
		this.posterFile = posterFile;
	}
	
	@Override
	public String toString() {
		return "Movie [movieId=" + movieId + ", title=" + title + ", director=" + director + ", genre=" + genre
				+ ", actors=" + actors + ", runtime=" + runtime + ", releaseDate=" + releaseDate + ", startDate="
				+ startDate + ", endDate=" + endDate + ", ticketPrice=" + ticketPrice + ", posterFile=" + posterFile
				+ ", nowPlaying=" + nowPlaying + "]";
	}

}
