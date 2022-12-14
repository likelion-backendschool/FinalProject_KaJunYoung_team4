package com.finalProject.mutbook.service.post;

import com.finalProject.mutbook.app.util.Ut;
import com.finalProject.mutbook.domain.post.Post;
import com.finalProject.mutbook.domain.member.Member;
import com.finalProject.mutbook.domain.post.PostRepository;
import com.finalProject.mutbook.service.tag.HashTagService;
import com.finalProject.mutbook.web.dto.post.CreatePostDto;
import com.finalProject.mutbook.web.dto.post.ModifyPostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    private final HashTagService hashTagService;

    /** 새로운 게시물을 저장하는 메소드
     * @param member 현재 로그인된 Member
     * @param createPostDto subject, content가 포함된 DTO
     */
    public Post savePost(Member member, CreatePostDto createPostDto) {
        Post newPost = Post.builder()
                .subject(createPostDto.getSubject())
                .content(createPostDto.getContent())
                .contentHtml(Ut.html.markdown(createPostDto.getContent()))
                .author(member)
                .build();

        postRepository.save(newPost);

        if (createPostDto.getHashTagContents() != null) {
            hashTagService.applyHashTags(newPost, createPostDto.getHashTagContents());
        }

        return newPost;
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

        hashTagService.applyHashTags(currentPost, modifyPostDto.getHashTagContents());
    }
}
