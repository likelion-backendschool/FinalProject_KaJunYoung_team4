package com.finalProject.weekOne.service.post;

import com.finalProject.weekOne.domain.app.util.Ut;
import com.finalProject.weekOne.domain.member.Member;
import com.finalProject.weekOne.domain.post.Post;
import com.finalProject.weekOne.domain.post.PostRepository;
import com.finalProject.weekOne.web.dto.post.CreatePostDto;
import com.finalProject.weekOne.web.dto.post.ModifyPostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    /** 새로운 게시물을 저장하는 메소드
     * @param member 현재 로그인된 Member
     * @param createPostDto subject, content가 포함된 DTO
     */
    public Post savePost(Member member, CreatePostDto createPostDto) {
        Post newPost = Post.builder()
                .subject(createPostDto.getSubject())
                .content(createPostDto.getContent())
                .contentHtml(Ut.html.markdown(createPostDto.getContent()))
                .createDate(LocalDateTime.now())
                .author(member)
                .build();

        return postRepository.save(newPost);
    }

    /** id로 Post 객체를 찾는 메소드
     * @param id 찾으려는 post.id 번호
     */
    public Post findByPostId(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    /** 모든 Post를 불러오는 메소드
     */
    public List<Post> findAllPost() {
        return postRepository.findAll();
    }

    /** Post를 삭제하는 메소드
     * @param currentPost id를 통해 찾은 Post
     */
    public void removePost(Post currentPost) {
        postRepository.delete(currentPost);
    }

    /**
     * Post를 수정하는 메소드
     *
     * @param currentPost 수정하기 전 Post
     * @param modifyPostDto Post의 subject, content를 가지고 있는 DTO
     */
    @Transactional
    public void modifyPost(Post currentPost, ModifyPostDto modifyPostDto) {
        currentPost.modifyPost(modifyPostDto.getSubject(),
                modifyPostDto.getContent(),
                Ut.html.markdown(modifyPostDto.getContent())
        );
    }
}
